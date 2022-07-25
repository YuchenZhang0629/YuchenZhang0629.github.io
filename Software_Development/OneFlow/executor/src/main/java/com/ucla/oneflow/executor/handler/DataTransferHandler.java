package com.ucla.oneflow.executor.handler;

import com.ucla.oneflow.executor.job.DataLoaderJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class DataTransferHandler implements RowCallbackHandler {

    private static final Logger appLogger = LoggerFactory.getLogger(DataTransferHandler.class);

    int cache = 10;
    private List<Map<String,Object>> rowList = new ArrayList<>();
    private JdbcTemplate destJtm;
    private String destTable;
    public DataTransferHandler(JdbcTemplate destJtm, String destTable) {
        this.destJtm = destJtm;
        this.destTable = destTable;
    }
    @Override
    public void processRow(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colCount = metaData.getColumnCount();
        Map<String,Object> row = new HashMap<>();
        for (int i = 1; i <= colCount; i++) {
            String[] strArr = metaData.getColumnName(i).split("\\.");
            row.put(strArr[1],resultSet.getObject(i));
        }
        rowList.add(row);
        if (rowList.size() == cache) {
            batchInsert(rowList);
            rowList.clear();
        }
    }
    public void saveRest() {
        if (!rowList.isEmpty()) {
            batchInsert(rowList);
        }
    }
    private void batchInsert(List<Map<String,Object>> res) {
        Set<String> keySet = res.get(0).keySet();
        List<String> keyList = new ArrayList(keySet);
        List<String> valueStrList = new ArrayList<>();
        res.forEach(rowMap ->{
            List<String> valueList = new ArrayList();
            keyList.forEach(key -> {
                valueList.add("'"+String.valueOf(rowMap.get(key))+"'");
            });
            String valueStr = "(''"+String.join(",",valueList)+")";
            valueStrList.add(valueStr);
        });
        destJtm.execute("insert into " + destTable + "("+String.join(",",keyList)+") values("+String.join(",",valueStrList)+")");
    }
}

package spark.test

// Import the packages
import org.apache.log4j.Logger
import java.io.File
import org.apache.spark.sql.{Row,SaveMode,SparkSession}

object SparkHiveExample {

  case class Record(key: Int, value: String)

  def main(args: Array[String]): Unit = {
    // For debug and improvement purposes
    // Upload a jar file to the server to execute
    val log = Logger.getLogger(getClass.getName)


    // This is a fixed configuration
    val warehouseLocation = new File("spark-warehouse").getAbsolutePath
    log.info(warehouseLocation)


    // This part is also fixed
    val spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._
    import spark.sql

    // Creating a table and loading data into it.
    // We can see how aggregation queries are also supported
    sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive")
    sql("LOAD DATA LOCAL INPATH '/dev/shm/kv1.txt' INTO TABLE src")
    sql("SELECT * FROM src").show()
    //  +---+-------+
    //  |key|  value|
    //  +---+-------+
    //  |238|val_238|
    //  | 86| val_86|
    //  |311|val_311|
    //  ...


    sql("SELECT COUNT(*) FROM src").show()
    //  +--------+
    //  |count(1)|
    //  +--------+
    //  |    500 |
    //  +--------+

    // sqlDF support all normal functions
    val sqlDF = sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key")

    //  Expand each row and add prefixes like Key and Value,
    //  allowing us to access each column by ordinal.
    val stringsDS = sqlDF.map {
      case Row(key: Int, value: String) => s"Key: $key, Value: $value"
    }
    stringsDS.show()
    //   +--------------------+
    //   |               value|
    //   +--------------------+
    //   |Key: 0, Value: val_0|
    //   |Key: 0, Value: val_0|
    //   |Key: 0, Value: val_0|
    //   ...



    // Creating temporary views within a SparkSession.
    val recordsDF = spark.createDataFrame((1 to 100).map(i => Record(i, s"val_$i")))
    recordsDF.createOrReplaceTempView("records")

    // Queries can then join DataFrame data with data stored in Hive.
    // HOWEVER: this is not saved in Hive, YET!
    sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show()
    //   +---+------+---+------+
    //   |key| value|key| value|
    //   +---+------+---+------+
    //   |  2| val_2|  2| val_2|
    //   |  4| val_4|  4| val_4|
    //   |  5| val_5|  5| val_5|
    //   ...


    """
      Store the table in Hive using HQL syntax instead of Spark SQL native syntax
      Difference:
        External tables: i.e. uploading csv based files, needs to have "EXTERNAL" keyword
        Internal: usually use the PARQUET format as shown right here
      To do this, Save DataFrame to Hive managed table and insert the data inside
      """
    sql("CREATE TABLE hive_records(key int, value string) STORED AS PARQUET")
    val df = spark.table("src")
    df.write.mode(SaveMode.Overwrite).saveAsTable("hive_records")
    sql("SELECT * FROM hive_records").show()


    // Prepare a Parquet data directory and Create a Hive external Parquet table
    // Notice difference between saving to table: saveAsTable() and here, to the directory
    val dataDir = "/tmp/parquet_data"
    spark.range(10).write.parquet(dataDir)
    sql(s"CREATE EXTERNAL TABLE hive_bigints(id bigint) STORED AS PARQUET LOCATION '$dataDir'")
    sql("SELECT * FROM hive_bigints").show()
    // +---+
    // | id|
    // +---+
    // |  0|
    // |  1|
    // |  2|
    // ... Order may vary, as spark processes the partitions in parallel.


    // Turn on flag for Hive Dynamic Partitioning
    // Create a Hive partitioned table using DataFrame API
    spark.sqlContext.setConf("hive.exec.dynamic.partition", "true")
    spark.sqlContext.setConf("hive.exec.dynamic.partition.mode", "nonstrict")
    df.write.partitionBy("key").format("hive").saveAsTable("hive_part_tbl")
    sql("SELECT * FROM hive_part_tbl").show()
    // +-------+---+
    // |  value|key|
    // +-------+---+
    // |val_238|238|
    // | val_86| 86|
    // |val_311|311|
    // ...

    spark.stop()
  }
}


---
layout: post
title: Blog2 - IMDB Scraping
---

##### Introduction
#### In this project, I am going to implement a cool and effective web scraper that allows me to find the other movies that share the most actors with my own favorite. Here is how I set up the project, allow me to expand on it section by section.


##### PART1: Loading the packages
```python
import scrapy
from scrapy.spiders import Spider
from scrapy.http import Request
```

##### PART2: Implementation of the "parse" method
#### The "parse" method assumes that I start on the main page of a movie. It would recognize the "Cast & Crew" link on the upper right, and yields a request to navigate to the page. Here is the code for this method:
```python
def parse(self, response):
        # Getting the table where the cast link is located
        cast_boxes_contents = response.css("div.SubNav__SubNavContent-sc-11106ua-3.cKmYsV").css("li.ipc-inline-list__item a")[0]
        # Getting the suffix that leads to next page, which is the cast list
        next_page_cast = cast_boxes_contents.attrib["href"]
        
        # Go to the next page
        if next_page_cast:
            cast_link = response.urljoin(next_page_cast)

            yield Request(cast_link, callback = self.parse_full_credits)
```

##### PART3: Implementation of the "parse_full_credits" method
#### The "parse" method assumes that I already navigated to the "Cast & Crew" page. By pinpointing the link for each actor, this method yields a request to finds the actor's URL that leads to the his or her personal page. Here is the code for this method.
```python
def parse_full_credits(self, response):
        # Extract the actors selector
        actors_selectors = response.css("table.cast_list tr")[1:]

        for actors in actors_selectors:
            # Check if this selector is indeed regarding an actor or just another element of the data frame
            if actors.css("a")[1]:
                actor_page_link = response.urljoin(actors.css("a")[1].css("a").attrib["href"])

                yield Request(actor_page_link, callback = self.parse_actor_page)
```

##### PART 4: Implementation of the "parse_actor_page" method
#### The "parse" method assumes that I already navigated to the actor's personal page, and recognizes the movies and shows that the actor involved in. It creates a dictionary of two elements: the actor's name, and the movies' or shows' names. It also yields the dictionary, which is convenient for me to do further analysis. Here is the code for this section.
```python
def parse_actor_page(self, response):
        # Retrieving information about the actor and his or her movies on the actors page
        movies_selector = response.css("div.filmo-category-section b")
        actor_name = response.css("span.itemprop")[0].css("::text").get()

        for movie_show in movies_selector:
            # Extracting the names and link that
            response.css("h1.header span").css("::text").get()
            movie_show_name = movie_show.css("::text").get()

            if movie_show_name:
                # Writing the
                yield {
                    "actor": actor_name,
                    "movie": movie_show_name
                }
```

##### PART 5: Data cleaning
#### After I obtained my output, I read the csv file with Jupyter Notebook and did a few data manipulation to find the number of shared actors for each of the movies with my favorite. Here are the results.
```python
import pandas as pd
Actors_Movies = pd.read_csv("output.csv")
Actors_Movies_aggregate = Actors_Movies.groupby("movie").aggregate(len)
Actors_Movies_aggregate.index.name = 'Movie'
Actors_Movies_aggregate.reset_index(inplace=True)
Actors_Movies_aggregate = Actors_Movies_aggregate.sort_values("actor", ascending = False)
Actors_Movies_aggregate.head(30)
```

<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>Movie</th>
      <th>actor</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>3287</th>
      <td>The Lord of the Rings: The Return of the King</td>
      <td>85</td>
    </tr>
    <tr>
      <th>3289</th>
      <td>The Lord of the Rings: The Two Towers</td>
      <td>53</td>
    </tr>
    <tr>
      <th>567</th>
      <td>Cameras in Middle-Earth</td>
      <td>46</td>
    </tr>
    <tr>
      <th>3285</th>
      <td>The Lord of the Rings: The Fellowship of the Ring</td>
      <td>37</td>
    </tr>
    <tr>
      <th>1680</th>
      <td>Lego the Lord of the Rings: The Video Game</td>
      <td>21</td>
    </tr>
    <tr>
      <th>3288</th>
      <td>The Lord of the Rings: The Third Age</td>
      <td>21</td>
    </tr>
    <tr>
      <th>3907</th>
      <td>Weta Workshop</td>
      <td>20</td>
    </tr>
    <tr>
      <th>822</th>
      <td>Designing Middle-Earth</td>
      <td>19</td>
    </tr>
    <tr>
      <th>2519</th>
      <td>Shortland Street</td>
      <td>18</td>
    </tr>
    <tr>
      <th>3160</th>
      <td>The Hobbit: An Unexpected Journey</td>
      <td>17</td>
    </tr>
    <tr>
      <th>958</th>
      <td>Entertainment Tonight</td>
      <td>17</td>
    </tr>
    <tr>
      <th>3165</th>
      <td>The Hobbit: The Battle of the Five Armies</td>
      <td>16</td>
    </tr>
    <tr>
      <th>3996</th>
      <td>Xena: Warrior Princess</td>
      <td>16</td>
    </tr>
    <tr>
      <th>126</th>
      <td>A Filmmaker's Journey: Making 'the Return of t...</td>
      <td>15</td>
    </tr>
    <tr>
      <th>1777</th>
      <td>Made in Hollywood</td>
      <td>15</td>
    </tr>
    <tr>
      <th>2340</th>
      <td>Reunited Apart</td>
      <td>15</td>
    </tr>
    <tr>
      <th>2041</th>
      <td>On the Set: The Lord of the Rings: The Two Towers</td>
      <td>14</td>
    </tr>
    <tr>
      <th>3322</th>
      <td>The Making of 'The Lord of the Rings'</td>
      <td>14</td>
    </tr>
    <tr>
      <th>1328</th>
      <td>Hercules: The Legendary Journeys</td>
      <td>14</td>
    </tr>
    <tr>
      <th>1353</th>
      <td>Hollywood Insider</td>
      <td>13</td>
    </tr>
    <tr>
      <th>746</th>
      <td>DNZ: The Real Middle Earth</td>
      <td>13</td>
    </tr>
    <tr>
      <th>3745</th>
      <td>Troldspejlet</td>
      <td>13</td>
    </tr>
    <tr>
      <th>3247</th>
      <td>The Late Show with Stephen Colbert</td>
      <td>13</td>
    </tr>
    <tr>
      <th>3086</th>
      <td>The Fellowship of the Cast</td>
      <td>13</td>
    </tr>
    <tr>
      <th>154</th>
      <td>A Passage to Middle-earth: The Making of 'Lord...</td>
      <td>12</td>
    </tr>
    <tr>
      <th>2273</th>
      <td>Quest for the Ring</td>
      <td>12</td>
    </tr>
    <tr>
      <th>2362</th>
      <td>Ringers: Lord of the Fans</td>
      <td>12</td>
    </tr>
    <tr>
      <th>1273</th>
      <td>HBO First Look</td>
      <td>12</td>
    </tr>
    <tr>
      <th>3422</th>
      <td>The Passing of an Age</td>
      <td>12</td>
    </tr>
    <tr>
      <th>2439</th>
      <td>Scale</td>
      <td>11</td>
    </tr>
  </tbody>
</table>
</div>

##### PART6: Recommendation
#### This scraper is really helpful if we want to find the relevant movies based on our favorite actors or actresses. Apart from that, it is very possible to scrape with the release date, countries, and even genre as well. I will be interested in seeing how to implement the techniques I learned into achieving those as well.

##### PART7: Conclusion and appendment
#### In this project, I got familiar with some of the most useful web-scraping techniques. Another valuable thing I achieved is that by using developer tools, I got first-hand experience with the most fundamental data structures in creating a website, which definitely sheds light into my potential future endeavors. That's so much for my project, see you next time!

##### Here is the complete code fot the scraper:
```python
import scrapy
from scrapy.spiders import Spider
from scrapy.http import Request

class QuoteSpider(scrapy.Spider):
    name = "imdb_spider"

    start_urls = ["https://www.imdb.com/title/tt0167260/?ref_=tt_ql_cl"]

    
    def parse(self, response):
        # Getting the table where the cast link is located
        cast_boxes_contents = response.css("div.SubNav__SubNavContent-sc-11106ua-3.cKmYsV").css("li.ipc-inline-list__item a")[0]
        # Getting the suffix that leads to next page, which is the cast list
        next_page_cast = cast_boxes_contents.attrib["href"]
        
        # Go to the next page
        if next_page_cast:
            cast_link = response.urljoin(next_page_cast)

            yield Request(cast_link, callback = self.parse_full_credits)
            

    def parse_full_credits(self, response):
        # Extract the actors selector
        actors_selectors = response.css("table.cast_list tr")[1:]

        for actors in actors_selectors:

            if actors.css("a")[1]:
                actor_page_link = response.urljoin(actors.css("a")[1].css("a").attrib["href"])

                yield Request(actor_page_link, callback = self.parse_actor_page)

        
    def parse_actor_page(self, response):
        # Retrieving information about the actor and his or her movies on the actors page
        movies_selector = response.css("div.filmo-category-section b")
        actor_name = response.css("span.itemprop")[0].css("::text").get()

        for movie_show in movies_selector:
            # Extracting the names and link that
            response.css("h1.header span").css("::text").get()
            movie_show_name = movie_show.css("::text").get()

            if movie_show_name:

                yield {
                    "actor": actor_name,
                    "movie": movie_show_name
                }
```
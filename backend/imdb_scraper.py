from bs4 import BeautifulSoup
from collections import deque
import requests
import json
import queue
import threading

# Does a Google search and retrieves the first IMDb link
def get_movie_url(query):
    query = query.replace(' ', '+') + '+imdb'
    query_url = 'https://www.google.com/search?q=' + query
    print(query_url)
    r = requests.get(query_url, headers={'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0 Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0'})
    soup = BeautifulSoup(r.text, "html5lib")

    for link in soup.find_all("a"):
        try:
            href = link.get('href')
        except requests.exceptions.ConnectionError:
            continue
        if href and 'imdb.com/title' in href:
            return href
    return ''

# Get movie information from IMDb given a url
def get_movie_info(url):
    r = requests.get(url, headers={'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0 Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0'})
    soup = BeautifulSoup(r.text, "html5lib")
    language = get_data_from_tag(soup, 'h4', 'Language:', 2)
    genre = get_data_from_tag(soup, 'h4', 'Genres:', 2)

    everything = json.loads(soup.find('script', {'type':'application/ld+json'}).get_text(strip=True))
    release_date = everything['datePublished'] if 'datePublished' in everything else ''
    content_title = everything['name'] if 'name' in everything else ''
    rating = everything['aggregateRating']['ratingValue'] if 'aggregateRating' in everything else ''
    imdb_url = everything['url'] if 'url' in everything else ''
    thumbnail = everything['image'] if 'image' in everything else '' # TODO: do something with thumbnail

    # If the content is a movie, get director; if it's a tv show, get creator
    director = ''
    if 'director' in everything:
        directors = everything['director']
        director = directors['name'] if type(directors) is dict and 'name' in directors else ''
    elif 'creator' in everything:
        creators = everything['creator']
        # print(creators)
        creator = creators[0] if type(creators) is list else creators
        if 'name' in creator:
            director = creator['name']

    # print(json.dumps(everything, indent=4, sort_keys=True))

    duration = None
    if 'duration' in everything:
        duration = get_duration_from_string(everything['duration'])
    else:
        duration = get_duration_from_string(get_data_from_tag(soup, 'time', 'datetime', 3))

    return {'name':content_title, 'genre': genre, 'rating': rating,
            'release_date': release_date, 'language': language, 'director': director,
            'duration': duration, 'imdb': imdb_url, 'thumbnail': thumbnail}

'''
next_state = 0 --> doesn't need sibling
next_state = 1 --> needs next_sibling
next_state = 2 --> needs next
'''
def get_data_from_tag(soup, tag, name, next_sibling):
    # This is slightly convoluted, but I can't see a better way to do this
    result = soup.find(tag, text=name) if next_sibling != 3 else soup.find(tag)
    if next_sibling == 1 and result:
        return result.next_sibling.strip()
    elif next_sibling == 2 and result:
        return result.find_next().text.strip()
    elif next_sibling == 3 and result:
        return result[name].strip()
    else:
        data_tag = soup.find(tag, {'itemprop':name})
        if data_tag:
            return data_tag.get_text(strip=True)
    return ''

def crawl(query):
    to_complete = set()
    completed = set()
    to_complete.add(get_movie_url(query))
    all_movie_info = []

    while len(to_complete) > 0 and len(all_movie_info) < 100:
        url = to_complete.pop()
        response = get_movie_info(url)
        all_movie_info.append(response[0])
        # print(response[1])
        for adjacent in response[1]:
            if adjacent not in completed and len(to_complete) < 100:
                to_complete.add(adjacent)
        completed.add(response[0]['imdb'])
    # print(all_movie_info)

# Crawls IMDb given a seed url
def crawl_imdb(seed_url, count):
    movie_urls = queue.Queue() # full links to content
    completed = set() # endpoints of visited content
    adjacent_urls = deque([seed_url]) # adjacent endpoints to visit
    pending = set()
    pending.add(seed_url)

    while len(adjacent_urls) > 0 and movie_urls.qsize() <= count:
        link = adjacent_urls.popleft()
        full_link = "https://imdb.com" + link
        pending.remove(link)
        if "/title" in link: # to not include seed url of "https://imdb.com"
            movie_urls.put(full_link)
            completed.add(link)
            global_completed.add(link)
        print(full_link)

        # If we already have enough links completed or pending, don't add any more
        if movie_urls.qsize() + len(adjacent_urls) > count:
            continue

        try:
            r = requests.get(full_link, headers={'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0 Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0'})
        except:
            continue
        soup = BeautifulSoup(r.text, "html5lib")

        for link in soup.find_all("a"):
            try:
                href = link.get('href')
            except requests.exceptions.ConnectionError:
                continue

            if href and href.startswith("/title"):
                imdb_end = href.split('?')[0][:16] # extracts something like 'title/tt5734576'
                if imdb_end not in completed and imdb_end not in pending and imdb_end not in global_completed:
                    print("Found " + imdb_end)
                    adjacent_urls.append(imdb_end)
                    pending.add(imdb_end)

    return movie_urls

# Hits add_content endpoint to add content to table
def add_content(content):
    r = requests.post("https://nosqls411.web.illinois.edu/add_content.php", data=content)
    print(r.status_code, r.reason, r.text)
    return r

# Hits add_content_meta endpoint to add content_meta to table
def add_content_meta(content_meta):
    r = requests.post("https://nosqls411.web.illinois.edu/add_content_meta.php", data=content_meta)
    print(r.status_code, r.reason, r.text)
    return r

# Takes a query, does the google search, then the IMDb scrape
def get_content_from_query(query):
    url = get_movie_url(query)
    return get_movie_info(url)

# Takes a seed url, crawls through imdb for urls, then gets movie info for each
def crawl_and_get_content(seed_url, count, num_threads):
    urls = crawl_imdb(seed_url, count)
    print(urls)
    bulk_add_content_from_urls(urls, num_threads)

def bulk_add_content_from_urls(urls, num_threads):
    # creating thread
    threads = []
    for i in range(num_threads):
        t1 = threading.Thread(target=handle_content_urls, args=(urls,))
        t1.start()
        threads.append(t1)
    for thread in threads:
        thread.join()

def handle_content_urls(urls):
    while urls.qsize() > 0:
        url = urls.get(block=False)
        if not url:
            return
        # Don't run on seed_url
        if '/title/' not in url:
            continue
        movie_info = get_movie_info(url)
        # If any field was not populated by IMDb, don't try adding it to the table
        all_populated = True
        for key, value in movie_info.items():
            if not value and not key == 'thumbnail':
                all_populated = False
        if all_populated:
            response = add_content(movie_info)
            if response.status_code == 200:
                message = json.loads(response.text)['response'].split(" ")[0]
                content_meta = {'contentId': message, 'imdb': movie_info['imdb'], 'thumbnail': movie_info['thumbnail']}
                # print(content_meta)
                add_content_meta(content_meta)
            print(movie_info)

def get_duration_from_string(duration_str):
    # duration_str of the form PT2H22M, PT22M
    rest = duration_str[2:]
    mins = 0
    if 'H' in rest:
        parts = rest.split('H')
        mins += int(parts[0]) * 60
        rest = parts[1]
    if 'M' in rest:
        parts = rest.split('M')
        mins += int(parts[0])
    return mins

# Gets all content, retrieves the full content info, and adds content metadata to table
def populate_metadata():
    r = requests.get("https://nosqls411.web.illinois.edu/search_content.php")
    all_content = json.loads(r.text)['content']
    for movie in all_content:
        if int(movie['ContentId']) < 889:
            continue
        print(movie['Name'])
        content = get_content_from_query(movie['Name'])
        # print(movie['Name'], content['imdb'])
        content_meta = {'contentId': movie['ContentId'], 'imdb': content['imdb'], 'thumbnail': content['thumbnail']}
        add_content_meta(content_meta)

# print(get_movie_info('https://www.imdb.com/title/tt0944947/')) # Game of Thrones
# print(get_movie_info('https://www.imdb.com/title/tt0109830/')) # Forrest Gump
# print(get_movie_info('https://www.imdb.com/title/tt0119217/')) # Good Will Hunting
# print(crawl('forrest gump'))
# add_content('the office')
global_completed = set()
for i in range(0, 30):
    base = "/search/title?keywords=superhero&view=simple&start="
    # base = "/search/title?genres=superhero&view=simple&start="
    base += str(i * 50)
    base += "&explore=title_type,genres&ref_=adv_nxt"
    crawl_and_get_content(base, 60, 8)
    print(" -------------------- FINISHED " + str(i) + " -------------------- ")
# populate_metadata()

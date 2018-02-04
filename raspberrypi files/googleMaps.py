from googlemaps import googlemaps
from googleplaces import GooglePlaces
from geopy.geocoders import GoogleV3
from geopy.geocoders import Nominatim
import urllib, urllib2, json
from pygeocoder import Geocoder
from haversine import haversine
import responses
import requests.packages.urllib3
from pprint import pprint
requests.packages.urllib3.disable_warnings()
googleGeocodeUrl = 'http://maps.googleapis.com/maps/api/geocode/json?'

def get_coordinates(query, from_sensor=False):
    query = query.encode('utf-8')
    params = {
        'address': query,
        'sensor': "true" if from_sensor else "false"
    }
    url = googleGeocodeUrl + urllib.urlencode(params)
    json_response = urllib.urlopen(url)
    response = json.loads(json_response.read())
    if response['results']:
        location = response['results'][0]['geometry']['location']
        latitude, longitude = location['lat'], location['lng']
        #print query, latitude, longitude
        #return latitude, longitude
    else:
        latitude, longitude = None, None
        print query, "<no results>"
    return latitude, longitude
global smallest
smallest = 20036
def find_closest(garage_lon, garage_lat, place_id):
    global closest_garage
    global smallest
    distance = haversine(my_lon, my_lat, garage_lon, garage_lat)
    if distance < smallest:
        smallest = distance
        closest_garage = place_id


gmaps = googlemaps.Client('AIzaSyBDZ-1yXXKyUJdodrsbsR4Um_XQcbhz4GU')

address = '47, South Bhopa Road, New Mandi, Muzaffarnagar, Uttar Pradesh, India'
my_lat , my_lon = get_coordinates(address)
print my_lat, my_lon
google_places = GooglePlaces('AIzaSyBDZ-1yXXKyUJdodrsbsR4Um_XQcbhz4GU')
local = google_places.nearby_search(location=address, keyword='Car Mechanic',
        radius=20000)
#local = gmaps.nearby_search('cafe near ' + address)
#print local.html_attributions
#all_stations = (local.places).json()['items']
print local.places
for place in local.places:
    print place.name
    print '\n'
    print place.geo_location['lat']
    print place.geo_location['lng']
    find_closest(place.geo_location['lng'], place.geo_location['lat'], place.place_id)
    #find_closest(place.geo_location['lat'], place.geo_location['lng'])
    print '\n'
    print place.place_id
    print '\n'
    place.get_details()
    #print place.details # A dict matching the JSON response from Google.
    print place.local_phone_number
    print place.international_phone_number
    print place.website
    print place.url
for place in local.places:
    if place.place_id==closest_garage:
        print 'closest garage is - '
        print place.name
        print place.geo_location['lat']
        print place.geo_location['lng']
#closest_stn = find_closest()
#print local['responseData']['results'][0]['titleNoFormatting']
resp = requests.get('https://maps.google.com', verify='C:\Python27\Lib\site-packages\certifi\cacert.pem')
results = Geocoder.reverse_geocode(29.4726817, 77.7085091)
print results.city
print results.country
print results.state
print results.street_address

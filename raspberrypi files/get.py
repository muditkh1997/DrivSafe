#!/usr/bin/python
import serial
import time
import requests
import json
from firebase import firebase
from random import randint

from googlemaps import googlemaps
from googleplaces import GooglePlaces
from geopy.geocoders import GoogleV3
from geopy.geocoders import Nominatim
import urllib, urllib2, json
from pygeocoder import Geocoder
from haversine import haversine
import responses
import requests.packages.urllib3
requests.packages.urllib3.disable_warnings()
googleGeocodeUrl = 'http://maps.googleapis.com/maps/api/geocode/json?'
gmaps = googlemaps.Client('AIzaSyBDZ-1yXXKyUJdodrsbsR4Um_XQcbhz4GU')

google_places = GooglePlaces('AIzaSyBDZ-1yXXKyUJdodrsbsR4Um_XQcbhz4GU')
while 1:
        print 'hi how are you'
        firebase_url = 'https://drivsafe-e0083.firebaseio.com/'
        firebase_url2 = firebase_url + 'Mechanic/'
        firebase_url4 = firebase.FirebaseApplication(firebase_url)
        firebase_url3 = firebase.FirebaseApplication(firebase_url2)
        address = firebase_url3.get('address',None)
        my_lat = firebase_url3.get('lat',None)
        my_lng = firebase_url3.get('long',None)
        flag = firebase_url3.get('flag',None)
        if flag==1:
                local = google_places.nearby_search(location=address, keyword='$
                        radius=2000)
                #local = gmaps.nearby_search('cafe near ' + address)
                #print local.html_attributions
                 #all_stations = (local.places).json()['items']
                i=0
                location = 'Mechanic_data'
                for place in local.places:
                        #print place.name
                        #print '\n'
                        #print place.geo_location['lat']
                        #print place.geo_location['lng']
                        #find_closest(place.geo_location['lng'], place.geo_location['lat'], place.place_id)
                        #find_closest(place.geo_location['lat'], place.geo_location['lng'])
                        #print '\n'
                        #print place.place_id
                        #print '\n'
                        place.get_details()
                        #print place.details # A dict matching the JSON response from Google.
                        #print place.local_phone_number
                        #print place.international_phone_number
                        #print place.website
                        #print place.url
                        latitude = place.geo_location['lat']
                        longitude = place.geo_location['lng']
                        distance = haversine(my_lng, my_lat, place.geo_location['lat'], place.geo_location['lng'])
                        data = {'name':place.name, 'distance':distance, 'lat':str(latitude), 'lng':str(longitude), 'phone_no':place.local_phone_number , 'place_id':place.place_id
                        result = requests.post(firebase_url + '/' + location + '.json'  ,  data=json.dumps(data))
                        i=i+1
                        if i==10:
                                break
                result1=firebase_url4.put('Mechanic' , "flag" , 0)

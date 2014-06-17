#!/usr/bin/env python
# -*- coding: utf-8 -*-
from lxml import html
import requests
import sys
from urlparse import urljoin

cas_url = 'https://cas.utc.fr/cas/'
login = raw_input('Login: ')
passwd = raw_input('Password: ')

post_param = {
    'username': login,
    'password': passwd,
    'submit_btn': 'LOGIN',
}

login_page_response = requests.get(
    'https://cas.utc.fr/cas/login?service=http%3A%2F%2Fwebapplis.utc.fr%2Fent%2F')
login_page = html.fromstring(login_page_response.text)
post_param['lt'] = login_page.xpath('//input[@name="lt"]/@value')[0]
post_param['execution'] = login_page.xpath('//input[@name="execution"]/@value')[0]
post_param['_eventId'] = login_page.xpath('//input[@name="_eventId"]/@value')[0]
cookie = {'JSESSIONID': login_page_response.cookies['JSESSIONID']}

# The requests module automatically follows redirections so validation should have a 200 return code
# We have to dig into Response.history to find the ticket
# In validation.history[0] you should be able to find the cookie AND the ticket
validation = requests.post(
    'https://cas.utc.fr/cas/login?service=http%3A%2F%2Fwebapplis.utc.fr%2Fent%2F',
    data=post_param,
    cookies=cookie,
    verify=False,
    allow_redirects=False
)

try:
    tick = validation.headers['location'].split('ticket=')[1]
except:
    print 'No ticket found in the response, login or password incorrect. Exiting.'
    sys.exit()

# PROBLEM: When we get redirected to webapplis the ent consume the ticket before we do.
# SOLUTIION IMPLEMENTED: allow_redirects=False
user_validation = requests.get(
    'https://cas.utc.fr/cas/validate?ticket='+tick+'&service=http%3A%2F%2Fwebapplis.utc.fr%2Fent%2F'
)
result = user_validation.text.split('\n')
if len(result) == 3:
    user = result[1]
    print 'The server authenticated the user: ' + user
else:
    print 'Error'

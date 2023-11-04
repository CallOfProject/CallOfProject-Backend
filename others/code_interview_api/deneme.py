import json
import requests

CODE_EVALUATION_URL = u'https://api.hackerearth.com/v4/partner/code-evaluation/submissions/'
CLIENT_SECRET = '21e2f720fdaa55621e02ef0ae1925e9cf72b1a5f'

def execute(source_file_name, language):
    source = open(source_file_name, "r")
    input_file = open("input.txt", "r")
    callback = "https://client.com/callback/"

    data = {    
        'source': source.read(),
        'lang': language,
        'time_limit': 5,
        'memory_limit': 246323,
        'input': input_file.read(),
        'callback' : callback,
        'id': "6320145ec35f0333db46ee584cfe3a4fdffe0ac714c2.api.hackerearth.com"
    }

    headers = {"client-secret": CLIENT_SECRET}
    input_file.close()
    source.close()
    resp = requests.post(CODE_EVALUATION_URL, json=data, headers=headers)
    """
    This will also work:
    resp = requests.post(CODE_EVALUATION_URL, data=data, headers=headers)
    """
    dict = json.loads(resp.text)
    return dict

GET_STATUS_URL = u'https://api.hackerearth.com/v4/partner/code-evaluation/submissions/06360bb4-5d28-4abf-8ebb-159bc2f19a1f/'


def get_status():
    headers = {"client-secret": CLIENT_SECRET}
    resp = requests.get(GET_STATUS_URL, headers=headers)
    dict = json.loads(resp.text)
    return dict

#print(execute("Main.java", "JAVA14"))
print(get_status())

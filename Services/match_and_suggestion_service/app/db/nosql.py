from pymongo import MongoClient

mongo_uri = 'mongodb://root:12345@localhost:27017/cop_nosql_db?authSource=admin'

client = MongoClient(mongo_uri, uuidrepresentation='standard')

db = client['cop_nosql_db']

collection = db['project_tag']

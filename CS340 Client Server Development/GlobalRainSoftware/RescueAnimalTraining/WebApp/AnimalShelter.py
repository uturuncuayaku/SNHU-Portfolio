from pymongo import MongoClient
from bson.objectid import ObjectId
from datetime import datetime

class AnimalShelter(object):
    
    def __init__(self,__USER,__PASS, __HOST, __PORT, __DB, __COL):
        
        USER = __USER
        PASS = __PASS
        HOST = __HOST
        PORT = __PORT
        DB   = __DB
        COL  = __COL
        
        uri = 'mongodb://%s:%s@%s:%d' % (USER, PASS, HOST, PORT)
        self.client = MongoClient(uri)
        self.database = self.client['%s' % (DB)]
        self.collection = self.database['%s' % (COL)]
        
        
    def debug(self):
        
        print("MongoClient")
        print(uri)
        
        try:
            info = self.client.admin.command('connectionStatus')
            print(info["authInfo"])
            
        except Exception as e:
            print("client.admin.command::FAIL")
            exit()
            
        print(self.client, self.database, self.collection, sep="\n")
        print(datetime.now().strftime("%Y-%m-%d %H:%M:S"))
        
    
    def read(self, query:dict=None, projection:dict=None):
                
        try:
            
            if query is None:
                query = {}
            
            myCollection = self.collection
            
            if projection is not None:
                cursor_obj = myCollection.find(query, projection)
            else:
                cursor_obj = myCollection.find(query)
            
            return list(cursor_obj)
        
        except Exception as e:
            
            print(f"Error {e}")
            return list()
        
        
    def create(self, data):
        try:
            
            result = self.collection.insert_one(data)
            
            if result.acknowledged:
                return True
            else:
                return False
            
        except Exception as e:
            
            print(f"Error: {e}")
            return False
        
        
    def update(self, query, update_values):
        
        if not isinstance(query, dict) or not isinstance(update_values, dict):
            
            print("Zero documents modified. Both arguments must be dictionaries.")
            return 0
        
        try:
            
            set_value = {"$set":update_values}
            
            if len(update_values) > 1:
                result = self.collection.update_many(query, set_value)
                return result.modified_count
            
        except Exception as e:
            
            print("Zero documents modified. Unable to update document(s).")
            print(f"Error {e}")
            
            return 0
        
        
    def delete(self, query):
        
        if not isinstance(query, dict):
            return 0
        
        try:
            
            result = self.collection.delete_many(query)
            return result.deleted_count
        
        except Exception as e:
            print("Zero documents deleted.")
            return 0

        
# ZooKeeper Master - Back-end
Back-end service that provide API for work with any real ZooKeepers servers. 

Work in pair with
[front-end part](https://github.com/Tiunchik/zkmaster_front "front-end part")

## Features
* See all nodes(path + value) as tree-node for one command.
* Atomic cascade nodes delete from real server.
* Inject more that one node to real server.
* Export & Import to TXT format, all nodes from real server.
* Temporally cache(value of all nodes from server) for any amount of real servers. 

## Instruction
1. Make connection with you server by send request to 
   API:"get host-value(connect)" - ZKM back, will receive data from 
   a server and will know about this server in the future.
2. Then you can send any others requests.


## Request process api
localhost:8081 - ip & port your ZKM. (change it for your environment)

localhost:2181 - ip & port your server. (change it for your environment)

---
* register server in ZKM - 
  GET http://localhost:8081/api/zkm/data/localhost:2181
  
  body:
    ```
     {empty request body}
    ```
  response:
  ```
  {
   "path":"/",
   "value":"nodeValue",
   "name":"/",
   "children":[
      {
         "path":"/1",
         "value":"nodeValue",
         "name":"1",
         "children":[
            {
               "path":"/1/2-1",
               "value":"nodeValue",
               "name":"2-1",
               "children":[
                  {
                     "path":"/1/2-1/3-1",
                     "value":"nodeValue",
                     "name":"3-1",
                     "children":[
                        
                     ]
                  },
                  {
                     "path":"/1/2-1/3-2",
                     "value":"nodeValue",
                     "name":"3-2",
                     "children":[
                     ]
                  }
               ]
            }
         ]
      }
   ]
  }
  ```

---
* create node
  POST http://localhost:8081/api/zkm/data/localhost:2181

  body:
  ```
  {
    "path": "/crud-test-node",
    "value": "value"
  }
  ```
  response:
  ```
  {true}
  ```

  ---
* update node
  PUT http://localhost:8081/api/zkm/data/localhost:2181

  body:
   ```
  {
    "path": "/crud-test-node",
    "value": "crud-test-node-update&value-update"
  }
  ```
  response:
  ```
  {true}
  ```


  ---
* delete node 
  DELETE http://localhost:8081/api/zkm/data/localhost:2181/crud-test-node-update

  body:
  ```
  {empty body}
  ```
  response:
  ```
  {true}
  ```
    

  ---
* export TXT (see result in response value)
  GET http://localhost:8081/api/zkm/transform/localhost:2181

  body:
  ```
  {
    "nodePath": "/1/2-1",
    "type": "TXT"
  }
  ```
  response:
  ```
  [
     "/2-1 : v",
     "/2-1/3-1 : v",
     "/2-1/3-2 : v",
     "/2-1/3-1/4 : v",
     "/2-1/3-2/4 : v"
  ]
  ```


  ---
* import try TXT
  POST http://localhost:8081/api/zkm/transform/localhost:2181

  body:
  ```
    {
      "nodePath": "/1/2-2",
      "type": "TXT",
      "content": [
      "/i1 : value",
      "/i1/i2 : value",
      "/i1/i2/i3 : value",
      "/i1/i2/i3/i4 : value"
      ]
    }
  ```
  response:
  ```
  {true}
  ```
  

  ---
* data injection
  POST http://localhost:8081/api/zkm/data/injection
  
  body:
  ```
    {
      "sourceHost": "localhost:2181",
      "sourceNodePath": "/b1/b2",
      "targetHost": "localhost:2181",
      "targetNodePath": "/i1/i2",
      "updOldValues": false
    }
    ```
  response:
  ```
  {empty body}
  ```

## Contacts
Write to contributors emails. ))

# ZooKeeper Master - Back-end
Back-end service that provide API for work with any real ZooKeepers servers. 

Work in pair with
[front-end part](https://github.com/Tiunchik/zkmaster_front "front-end part")

## Features
* Atomic cascade nodes delete from real server.
* Inject more that one node to real server.
* See all nodes(path + value) for one command.
* Export & Import to TXT format, all nodes from real server.
* Temporal cache(value of all nodes from server) for any amount of real servers. 

## Instruction
1. Make connection with you server by send request to 
   API:"get host-value(connect)" - ZKM back, will receive data from 
   server and will know about this server in future.
2. Then you can send any others requests.


## Request process api
* get host-value(connect)
  GET http://localhost:8081/api/zkm/data/localhost:2181
  ```
  {empty request body}
  ```

* create node
  POST http://localhost:8081/api/zkm/data/localhost:2181
  ```
  {
    "path": "/crud-test-node",
    "value": "value"
  }
  ```
  
* update node
  PUT http://localhost:8081/api/zkm/data/localhost:2181
   ```
  {
    "path": "/crud-test-node",
    "value": "crud-test-node-update&value-update"
  }
  ```

* delete node 
  DELETE http://localhost:8081/api/zkm/data/localhost:2181/crud-test-node-update
  ```
  {empty body}
  ```
  
* export TXT (see result in response value)
  GET http://localhost:8081/api/zkm/transform/localhost:2181
  ```
  {
    "nodePath": "/1/2-1",
    "type": "TXT"
  }
  ```
  
* import try TXT
  POST http://localhost:8081/api/zkm/transform/localhost:2181
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
  
* data injection
  POST http://localhost:8081/api/zkm/data/injection
  ```
    {
    "sourceHost": "localhost:2181",
    "sourceNodePath": "/b1/b2",
    "targetHost": "localhost:2181",
    "targetNodePath": "/i1/i2",
    "updOldValues": false
    }
    ```

## Contacts
Write to contributors emails. ))

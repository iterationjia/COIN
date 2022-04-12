from py2neo import Graph
import json

g = Graph(
    'http://localhost:7474',
    user="neo4j",
    password="neo4j")
query = 'match p=(n1:FirstIndustry)-->(n2:SecondIndustry) return p limit 10 '
temp = g.run(query).data()

print(temp)

with open('temp.txt', 'w') as file_object:
    file_object.write(str(temp))


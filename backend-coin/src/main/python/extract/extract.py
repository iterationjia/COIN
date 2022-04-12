import node
import relation

graphId = 1000001
path = "C:\\Users\\Guo\\Desktop\\records.json"
json_str = "{"+node.node(graphId, path)+","+relation.relation(path)+"}"

with open('output.json', 'w') as file_object:
    file_object.write(json_str)
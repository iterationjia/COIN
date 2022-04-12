import json
import re


def node(graphId, path):
    graphID = graphId

    data1 = json.load(open(path, encoding='utf-8-sig'))
    data = json.dumps(data1)

    starts = [each.start() for each in re.finditer("\"start\": {|\"end\": {", data)]

    json_str = "\"nodes\": ["
    idSet = set()

    for x in starts:
        id_st = data.index("identity", x) + 11
        id_ed = data.index(",", id_st)
        labels_st = data.index("labels", x) + 11
        labels_ed = data.index("]", labels_st) - 1
        name_st = data.index("name", x) + 8
        name_ed = data.index(",", name_st) - 1
        ID = int(data[id_st:id_ed])
        if ID in idSet:
            continue
        idSet.add(ID)
        name = data[name_st:name_ed].encode('utf-8').decode('unicode_escape')
        labels = data[labels_st:labels_ed]

        color = ""

        if labels == "FirstIndustry":
            color = "#FF0000"
        elif labels == "SecondIndustry":
            color = "#FF8000"
        elif labels == "Company":
            color = "#FFFF00"
        elif labels == "Person":
            color = "#00FF00"
        elif labels == "Stock":
            color = "#00FFFF"
        elif labels == "PriceStatistic":
            color = "#FF00FF"
        elif labels == "Price":
            color = "#F781BE"
        else:
            print("labels wrong")

        dic = {'id': ID, 'name': name, 'color': color, 'node_size': 30, 'font_size': 20, 'x': 0, 'y': 0,
               'isShown': True,
               'shape': 'circle', 'graphId': graphID}

        json_str += json.dumps(dic, ensure_ascii=False, indent=2)
        json_str += ","

    json_str = json_str[0:len(json_str) - 1]
    json_str += "]"
    return json_str

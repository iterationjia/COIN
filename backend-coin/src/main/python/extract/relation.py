import json
import re


def relation(path):
    data1 = json.load(open(path, encoding='utf-8-sig'))
    data = json.dumps(data1)

    starts = [each.start() for each in re.finditer("relationship", data)]

    json_str = "\"links\": ["
    idSet = set()

    for x in starts:
        id_st = data.index("identity", x) + 11
        id_ed = data.index(",", id_st)
        source_st = data.index("start", x) + 8
        source_ed = data.index(",", source_st)
        target_st = data.index("end", x) + 6
        target_ed = data.index(",", target_st)
        name_st = data.index("name", x) + 8
        name_ed = data.index(",", name_st) - 1
        ID = int(data[id_st:id_ed])
        if ID in idSet:
            continue
        idSet.add(ID)
        source = int(data[source_st:source_ed])
        target = int(data[target_st:target_ed])
        name = data[name_st:name_ed].encode('utf-8').decode('unicode_escape')

        dic = {'id': ID, 'source': source, 'target': target, 'name': name, "isShown": False, "isSolid": True}

        json_str += json.dumps(dic, ensure_ascii=False, indent=2)
        json_str += ","

    json_str = json_str[0:len(json_str) - 1]
    json_str += "]"
    return json_str

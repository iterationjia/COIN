from py2neo import Graph

class AnswerSearcher:
    def __init__(self):
        self.g = Graph(
            'http://localhost:7474',
            user="neo4j",
            password="neo4j")
        self.num_limit = 20

    '''执行cypher查询，并返回相应结果'''
    def search_main(self, sqls):

        final_answers = []
        for sql_ in sqls:
            question_type = sql_['question_type']
            queries = sql_['sql']
            answers = []
            for query in queries:
                ress = self.g.run(query).data()
                answers += ress
            final_answer = self.answer_prettify(question_type, answers)
            if final_answer:
                final_answers.append(final_answer)
        return final_answers

    '''根据对应的qustion_type，调用相应的回复模板'''
    def answer_prettify(self, question_type, answers):
        final_answer = []
        if not answers:
            return ''
        if question_type == 'company_ceo':
            desc = [i['n2'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = 'ceo是'+ name[0]

        elif question_type == 'company_legal_representative':
            desc = [i['n2'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = '法人代表是'+ name[0]

        elif question_type == 'company_manager':
            desc = [i['n2'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = '高管有'
            for i in name:
                if i == name[len(name)-1]:
                    final_answer += i
                else:
                    final_answer += i+","

        elif question_type == 'first_industry_second_industry':
            desc = [i['n2'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = '二级行业是'+ name[0]

        elif question_type == 'second_industry_company':
            desc = [i['n2'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = '公司有'
            for i in name:
                if i == name[len(name)-1]:
                    final_answer += i
                else:
                    final_answer += i+","

        elif question_type == 'stock_company':
            desc = [i['n2'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = '公司是'+ name[0]

        elif question_type == 'company_average_education':
            desc = [i['n'] for i in answers]
            name = [i['average_education'] for i in desc]
            if name[0]=='middle':
                res='中学及以下水平'
            elif name[0]=='college':
                res='本科水平'
            else:
                res='本科以上水平'
            final_answer = '平均学历是'+ res

        elif question_type == 'company_infomation':
            desc = answers
            register_location = answers[0]['n.register_location']
            establish_date = answers[1]['n.establish_date']
            employee_num = answers[2]['n.employee_num']
            retire_rate = answers[3]['n.retire_rate']
            average_education = answers[4]['n.average_education']
            register_capital = answers[5]['n.register_capital']
            if average_education=='middle':
                res='中学及以下水平'
            elif average_education=='college':
                res='本科水平'
            else:
                res='本科以上水平'

            final_answer = '注册地是'+ register_location +'\n'+ '注册资本为'+ str(register_capital)+'元' +'\n''成立日期是'+ establish_date +'\n'+ '员工数为'+ str(employee_num) +'\n'+ '员工迭代率为'+ str(retire_rate) +'\n'+ '平均学历为'+ res

        elif question_type == 'stock_infomation':
            desc = [i['n'] for i in answers]
            stock_code = [i['stock_code'] for i in desc]
            start_date = [i['start_date'] for i in desc]
            st_rate = [i['st_rate'] for i in desc]
            final_answer = '股票的代码为'+ stock_code[0] +'\n'+ '上市日期是'+ start_date[0] +'\n'+ '股票st率为'+ str(st_rate[0])

        elif question_type == 'stock_date':
            desc = [i['n3'] for i in answers]
            _open = [i['open'] for i in desc]
            close = [i['close'] for i in desc]
            high = [i['high'] for i in desc]
            low = [i['low'] for i in desc]
            volume = [i['volume'] for i in desc]
            money = [i['money'] for i in desc]
            final_answer = '开盘价:'+ str(_open[0])+'￥' +'\n'+ '收盘价：'+ str(close[0])+'￥' +'\n'+ '最高价：'+ str(high[0])+'￥' +'\n'+ '最低价：'+ str(low[0])+'￥' +'\n'+ '成交数：'+ str(volume[0]) +'\n'+ '成交金额：'+ str(money[0])+'￥'

        elif question_type == 'person_company':
            desc = [i['n1'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = '与他\她有关公司有'
            for i in name:
                if i == name[len(name)-1]:
                    final_answer += i
                else:
                    final_answer += i+","

        elif question_type == 'company_second_industry':
            desc = [i['n2'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = '二级行业是'+ name[0]

        elif question_type == 'company_stock':
            desc = [i['n1'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = '股票是'+ name[0]

        elif question_type == 'Stock':
            desc = [i['n5'] for i in answers]
            name = [i['name'] for i in desc]
            final_answer = name[0]

        elif question_type == 'SecondIndustry':
            desc = [i['n5'] for i in answers]
            name = [i['name'] for i in desc]
            print('result')
            final_answer = name[0]

        elif question_type == 'stock_predict':
            desc = [i['n2'] for i in answers]
            ex7 = [i['_7_expected_price'] for i in desc]
            ex15=[i['_15_expected_price'] for i in desc]
            ex30=[i['_30_expected_price'] for i in desc]
            final_answer = '预计7天后价格为:'+format(ex7[0], '.3f')+'￥'+'\n'+'预计15天后价格为:'+format(ex15[0], '.3f')+'￥'+'\n'+'预计30天后价格为:'+format(ex30[0], '.3f')+'￥'

        elif question_type == 'person_infomation':
            desc = [i['n'] for i in answers]
            name = [i['name'] for i in desc]
            birth_year = [i['birth_year'] for i in desc]
            title_class = [i['title_class'] for i in desc]
            title_level = [i['title_level'] for i in desc]
            gender = [i['gender'] for i in desc]
            title = [i['title'] for i in desc]
            resume = [i['resume'] for i in desc]
            degree = [i['degree'] for i in desc]
            final_answer = name[0] +',性别为'+gender[0] +',出生于'+str(birth_year[0])+ ',学历为'+degree[0]+',职位为'+title[0]+',职位类别为'+title_class[0]+',职位级别为'+title_level[0]+',以下是这个人的档案信息:'+resume[0]
        return final_answer


if __name__ == '__main__':
    searcher = AnswerSearcher()
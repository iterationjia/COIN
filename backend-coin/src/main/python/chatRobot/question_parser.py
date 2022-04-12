class QuestionPaser:

    '''构建实体节点'''
    def build_entitydict(self, args):
        entity_dict = {}
        for arg, types in args.items():
            for type in types:
                if type not in entity_dict:
                    entity_dict[type] = [arg]
                else:
                    entity_dict[type].append(arg)

        return entity_dict

    '''解析主函数'''
    def parser_main(self, res_classify):
        args = res_classify['args']
        entity_dict = self.build_entitydict(args)
        question_types = res_classify['question_types']
        sqls = []

        if entity_dict.get('company')!=None:
            sql_ = {}
            sql_['question_type'] = 'SecondIndustry'
            sql_['sql'] =["match (n5:SecondIndustry)-[r]->(n6:Company) where n6.name='{0}' return n5".format(i) for i in entity_dict.get('company')]
            sqls.append(sql_)
            sql_ = {}
            sql_['question_type'] = 'Stock'
            sql_['sql'] =["match (n5:Stock)-[r]->(n6:Company) where n6.name ='{0}' return n5".format(i) for i in entity_dict.get('company')]
            sqls.append(sql_)

        elif entity_dict.get('stock')!=None:
            sql_ = {}
            sql_['question_type'] = 'SecondIndustry'
            sql_['sql'] =["match (n5:SecondIndustry)-[r]->(n6:Company)<--(n7:Stock) where n7.name='{0}' return n5".format(i) for i in entity_dict.get('stock')]
            sqls.append(sql_)
            print("STOCK")
            print(entity_dict.get('stock')[0])

        for question_type in question_types:
            sql_ = {}
            sql_['question_type'] = question_type
            sql = []
            if question_type == 'company_ceo':
                sql = self.sql_transfer(question_type, entity_dict.get('company'))

            elif question_type == 'company_legal_representative':
                sql = self.sql_transfer(question_type, entity_dict.get('company'))

            elif question_type == 'company_manager':
                sql = self.sql_transfer(question_type, entity_dict.get('company'))

            elif question_type == 'first_industry_second_industry':
                sql = self.sql_transfer(question_type, entity_dict.get('first_industry'))

            elif question_type == 'second_industry_company':
                sql = self.sql_transfer(question_type, entity_dict.get('second_industry'))

            elif question_type == 'company_average_education':
                sql = self.sql_transfer(question_type, entity_dict.get('company'))

            elif question_type == 'company_register_location':
                sql = self.sql_transfer(question_type, entity_dict.get('company'))

            elif question_type == 'stock_company':
                sql = self.sql_transfer(question_type, entity_dict.get('stock'))

            elif question_type == 'company_stock':
                sql = self.sql_transfer(question_type, entity_dict.get('company'))

            elif question_type == 'company_infomation':
                sql = self.sql_transfer(question_type, entity_dict.get('company'))

            elif question_type == 'stock_infomation':
                sql = self.sql_transfer(question_type, entity_dict.get('stock'))

            elif question_type == 'stock_date':
                sql = self.sql_transfer(question_type, entity_dict.get('stock')+entity_dict.get('date'))

            elif question_type == 'company_second_industry':
                sql = self.sql_transfer(question_type, entity_dict.get('company'))

            elif question_type == 'person_company':
                if entity_dict.get('manager')!=None:
                    sql = self.sql_transfer(question_type, entity_dict.get('manager'))
                elif entity_dict.get('ceo')!=None:
                    sql = self.sql_transfer(question_type, entity_dict.get('ceo'))
                elif entity_dict.get('legal_representative')!=None:
                    sql = self.sql_transfer(question_type, entity_dict.get('legal_representative'))

            elif question_type == 'person_infomation':
                if entity_dict.get('manager')!=None:
                    sql = self.sql_transfer(question_type, entity_dict.get('manager'))
                elif entity_dict.get('ceo')!=None:
                    sql = self.sql_transfer(question_type, entity_dict.get('ceo'))
                elif entity_dict.get('legal_representative')!=None:
                    sql = self.sql_transfer(question_type, entity_dict.get('legal_representative'))

            elif question_type == 'stock_predict':
                sql = self.sql_transfer(question_type, entity_dict.get('stock'))

            if sql:
                sql_['sql'] = sql

                sqls.append(sql_)

        return sqls

    '''针对不同的问题，分开进行处理'''
    def sql_transfer(self, question_type, entities):
        if not entities:
            return []

        # 查询语句
        sql = []
        # 查询公司CEO
        if question_type == 'company_ceo':
            sql = ["match (n1:Company)-[r]->(n2:Person) where r.name='ceo' and n1.name='{0}' return n2".format(i) for i in entities]

        # 查询法人
        elif question_type == 'company_legal_representative':
            sql = ["match (n1:Company)-[r]->(n2:Person) where r.name='法人' and n1.name='{0}' return n2".format(i) for i in entities]

        # 查询高管
        elif question_type == 'company_manager':
            sql = ["match (n1:Company)-[r]->(n2:Person) where r.name='高管' and n1.name='{0}' return n2".format(i) for i in entities]

        # 查询一级行业
        elif question_type == 'first_industry_second_industry':
            sql = ["match (n1:FirstIndustry)-[r]->(n2:SecondIndustry) where n1.name='{0}' return n2".format(i) for i in entities]

        # 查询二级行业
        elif question_type == 'second_industry_company':
            sql = ["match (n1:SecondIndustry)-[r]->(n2:Company) where n1.name='{0}' return n2".format(i) for i in entities]

        # 查询平均学历
        elif question_type == 'company_average_education':
            sql = ["match (n:Company) where n.name = '{0}' return n".format(i) for i in entities]

        # 查询股票的公司信息
        elif question_type == 'stock_company':
            sql = ["match (n1:Stock)-[r]->(n2:Company) where n1.name ='{0}' return n2".format(i) for i in entities]

            # 查询公司的股票信息
        elif question_type == 'company_stock':
            sql = ["match (n1:Stock)-[r]->(n2:Company) where n2.name ='{0}' return n1".format(i) for i in entities]

            # 查询公司信息
        elif question_type == 'company_infomation':
            sql1 = ["match (n:Company) where n.name = '{0}' return n.register_location".format(i) for i in entities]
            sql2 = ["match (n:Company) where n.name = '{0}' return n.establish_date".format(i) for i in entities]
            sql3 = ["match (n:Company) where n.name = '{0}' return n.employee_num".format(i) for i in entities]
            sql4 = ["match (n:Company) where n.name = '{0}' return n.retire_rate".format(i) for i in entities]
            sql5 = ["match (n:Company) where n.name = '{0}' return n.average_education".format(i) for i in entities]
            sql6 = ["match (n:Company) where n.name = '{0}' return n.register_capital".format(i) for i in entities]
            sql=sql1+sql2+sql3+sql4+sql5+sql6

        # 查询股票的信息
        elif question_type == 'stock_infomation':
            sql = ["match (n:Stock) where n.name = '{0}' return n".format(i) for i in entities]


            # 查询某日股票信息
        elif question_type == 'stock_date':
            sql = ["match (n1:Stock)-[r1]->(n2:PriceStatistic)-[r2]->(n3:Price) where n1.name = '{0}' and n3.date='{1}' return n3".format(entities[0],entities[1])]

            # 查询公司二级行业
        elif question_type == 'company_second_industry':
            sql = ["match (n1:Company)<-[r]-(n2:SecondIndustry) where n1.name='{0}'  return n2".format(i) for i in entities]

            # 查询人名对应公司
        elif question_type == 'person_company':
            sql = ["match (n1:Company)-[r]->(n2:Person) where n2.name='{0}'  return n1".format(i) for i in entities]

        elif question_type == 'stock_predict':
            sql = ["match (n1:Stock)-[r]->(n2:PriceStatistic) where n1.name='{}' return n2".format(i) for i in entities]

        elif question_type == 'person_infomation':
            sql = ["match (n:Person) where n.name='{0}'  return n".format(i) for i in entities]

        return sql



if __name__ == '__main__':
    handler = QuestionPaser()
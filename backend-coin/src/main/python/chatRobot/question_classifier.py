import os
import ahocorasick

class QuestionClassifier:
    def __init__(self):
        #　特征词路径
        pathPrefix = "src\\main\\resources\\dict\\"
        self.average_education_path =  pathPrefix+'average_education.txt'
        self.first_industry_path =  pathPrefix+'first_industry.txt'
        self.second_industry_path =  pathPrefix+'second_industry.txt'
        self.stock_path =  pathPrefix+'stock.txt'
        self.register_location_path =  pathPrefix+'register_location.txt'
        self.company_path =  pathPrefix+'company.txt'
        self.legal_representative_path =  pathPrefix+'legal_representative.txt'
        self.ceo_path =  pathPrefix+'ceo.txt'
        self.manager_path =  pathPrefix+'manager.txt'
        self.date_path =  pathPrefix+'date.txt'



        # 加载特征词
        self.company_wds= set([i.strip() for i in open(self.company_path,encoding='utf-8') if i.strip()])
        self.legal_representative_wds= set([i.strip() for i in open(self.legal_representative_path,encoding='utf-8') if i.strip()])
        self.register_location_wds= set([i.strip() for i in open(self.register_location_path,encoding='utf-8') if i.strip()])
        self.ceo_wds= set([i.strip() for i in open(self.ceo_path,encoding='utf-8') if i.strip()])
        self.manager_wds= set([i.strip() for i in open(self.manager_path,encoding='utf-8') if i.strip()])

        self.average_education_wds= set([i.strip() for i in open(self.average_education_path,encoding='utf-8') if i.strip()])
        self.first_industry_wds= set([i.strip() for i in open(self.first_industry_path,encoding='utf-8') if i.strip()])
        self.second_industry_wds= set([i.strip() for i in open(self.second_industry_path,encoding='utf-8') if i.strip()])
        self.stock_wds= set([i.strip() for i in open(self.stock_path,encoding='utf-8') if i.strip()])
        self.date_wds= set([i.strip() for i in open(self.date_path,encoding='utf-8') if i.strip()])


        #领域加载
        self.region_words = set.union(self.company_wds,self.legal_representative_wds,self.ceo_wds,self.manager_wds,self.average_education_wds,self.first_industry_wds,self.second_industry_wds,self.stock_wds,self.date_wds)
        # 构造领域actree
        self.region_tree = self.build_actree(list(self.region_words))
        # 构建词典
        self.wdtype_dict = self.build_wdtype_dict()
        # 问句疑问词
        self.company_qwds=['公司','企业']
        self.ceo_qwds=['首席执行官','ceo','CEO','总经理','行政总监']
        self.legal_representative_qwds=['法人','法人代表']
        self.register_location_qwds=['发源地','注册地','地址','地点']
        self.manager_qwds=['高管','高级管理']
        self.average_education_qwds=['学历','教育','文凭']
        self.first_industry_qwds=['第一产业','第一行业','一级行业','一级产业']
        self.second_industry_qwds=['产业','行业','第二产业','第二行业','二级行业','二级产业']
        self.stock_qwds=['持股','控股','股票']
        self.price_qwds=['开盘','价格','数据']
        self.predict_qwds=['预期','预估','预计','预测']


        print('model init finished ......')

        return

    '''分类主函数'''
    def classify(self, question):
        data = {}
        financial_dict = self.check_financial(question)
        if not financial_dict:
            return {}
        data['args'] = financial_dict
        #收集问句当中所涉及到的实体类型
        types = []
        for type_ in financial_dict.values():
            types += type_
        question_type = 'others'

        question_types = []

        if self.check_words(self.ceo_qwds, question) and ('company' in types):
            question_type = 'company_ceo'
            question_types.append(question_type)


        # 法人
        if self.check_words(self.legal_representative_qwds, question) and ('company' in types):
            question_type = 'company_legal_representative'
            question_types.append(question_type)

        # 高管
        if self.check_words(self.manager_qwds, question) and ('company' in types):
            question_type = 'company_manager'
            question_types.append(question_type)

        # 一级行业
        if self.check_words(self.second_industry_qwds, question) and ('first_industry' in types) :
            question_type = 'first_industry_second_industry'
            question_types.append(question_type)
            #二级行业
        elif self.check_words(self.company_qwds, question) and ('second_industry' in types):
            question_type = 'second_industry_company'
            question_types.append(question_type)

        #平均学历
        if self.check_words(self.average_education_qwds, question) and ('company' in types):
            question_type = 'company_average_education'
            question_types.append(question_type)

        #注册地
        if self.check_words(self.register_location_qwds, question) and ('company' in types):
            question_type = 'company_register_location'
            question_types.append(question_type)


        #公司信息获取股票名
        if self.check_words(self.stock_qwds, question) and ('company' in types):
            question_type = 'company_stock'
            question_types.append(question_type)

        #股票名获取公司信息
        if self.check_words(self.company_qwds, question) and ('stock' in types):
            question_type = 'stock_company'
            question_types.append(question_type)

        #某日股票信息
        if self.check_words(self.price_qwds, question) and ('date' in types):
            question_type = 'stock_date'
            question_types.append(question_type)

        #人名对应公司
        if self.check_words(self.company_qwds, question) and ('ceo' in types):
            question_type = 'person_company'
            question_types.append(question_type)
        elif self.check_words(self.company_qwds, question) and ('manager' in types):
            question_type = 'person_company'
            question_types.append(question_type)
        elif self.check_words(self.company_qwds, question) and ('legal_representative' in types):
            question_type = 'person_company'
            question_types.append(question_type)

        #公司二级行业
        if self.check_words(self.second_industry_qwds, question) and ('company' in types):
            question_type = 'company_second_industry'
            question_types.append(question_type)

        #预期价格
        if self.check_words(self.predict_qwds, question) and ('stock' in types):
            question_type = 'stock_predict'
            question_types.append(question_type)

        # 若没有查到相关的外部查询信息，那么则将该公司的描述信息返回
        if question_types == [] and 'company' in types:
            question_types = ['company_infomation']

        # 若没有查到相关的外部查询信息，那么则将该股票的描述信息返回
        if question_types == [] and 'stock' in types:
            question_types = ['stock_infomation']

        # 若没有查到相关的外部查询信息，那么则将该人物的描述信息返回
        if question_types == [] and 'ceo' in types:
            question_types = ['person_infomation']
        elif question_types == [] and 'manager' in types:
            question_types = ['person_infomation']
        elif question_types == [] and 'legal_representative' in types:
            question_types = ['person_infomation']

        # 将多个分类结果进行合并处理，组装成一个字典
        data['question_types'] = question_types

        return data

    '''构造词对应的类型'''
    def build_wdtype_dict(self):
        wd_dict = dict()
        for wd in self.region_words:
            wd_dict[wd] = []
            if wd in self.company_wds:
                wd_dict[wd].append('company')
            if wd in self.stock_wds and 'company' not in wd_dict :
                wd_dict[wd].append('stock')
            if wd in self.date_wds:
                wd_dict[wd].append('date')
            if wd in self.legal_representative_wds:
                wd_dict[wd].append('legal_representative')
            if wd in self.manager_wds:
                wd_dict[wd].append('manager')
            if wd in self.ceo_wds:
                wd_dict[wd].append('ceo')
            if wd in self.average_education_wds:
                wd_dict[wd].append('average_education')
            if wd in self.first_industry_wds:
                wd_dict[wd].append('first_industry')
            if wd in self.second_industry_wds:
                wd_dict[wd].append('second_industry')
        return wd_dict

    '''构造actree，加速过滤'''
    def build_actree(self, wordlist):
        actree = ahocorasick.Automaton()
        for index, word in enumerate(wordlist):
            actree.add_word(word, (index, word))
        actree.make_automaton()
        return actree

    '''问句过滤'''
    def check_financial(self, question):
        region_wds = []
        for i in self.region_tree.iter(question):
            wd = i[1][1]
            region_wds.append(wd)
        stop_wds = []
        for wd1 in region_wds:
            for wd2 in region_wds:
                if wd1 in wd2 and wd1 != wd2:
                    stop_wds.append(wd1)
        final_wds = [i for i in region_wds if i not in stop_wds]
        final_dict = {i:self.wdtype_dict.get(i) for i in final_wds}

        return final_dict

    '''基于特征词进行分类'''
    def check_words(self, wds, sent):
        for wd in wds:
            if wd in sent:
                return True
        return False


if __name__ == '__main__':
    handler = QuestionClassifier()
    while 1:
        question = input('input an question:')
        data = handler.classify(question)
        print(data)
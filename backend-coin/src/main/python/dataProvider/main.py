from jqdatasdk import *
from datetime import date, timedelta
import pandas as pd

auth('13270018865', '018865')
# auth('18851192208', 'Acoin2021')
# auth('18585285811', '285811')
d = date.today() + timedelta(days=-2)

print(get_query_count())

# 1 股票
# 1.1 股票概况

# 获取所有正在上市的股票信息
# data1 = get_all_securities(date=d)
# open('joinQuant\\stock_basic.csv', 'w')
# data1.to_csv('joinQuant\\stock_basic.csv', encoding='gbk')
# print(data1.head())

stock_csv = pd.read_csv('../../../../../../../课程/软件工程与计算III/迭代三/JoinQuant/joinQuant/stock_basic.csv', encoding='gbk')
stock_list = stock_csv[stock_csv.columns[0]].values.tolist()

# 得到上市股票在一年内是否是ST
# data2 = get_extras('is_st', stock_list, start_date=None, end_date=d, df=True, count=365)
# data2.to_csv('joinQuant\\stock_st.csv', encoding='gbk')
# print(data2.head())

# 1.2 股票行情数据
# 时间周期的行情数据
# print(len(stock_list))
# for i in range(len(stock_list)):
#     data3 = get_bars(stock_list[i], count=365, unit='1d',
#                      fields=['date', 'open', 'close', 'high', 'low', 'volume', 'money'],
#                      end_dt=d)
#     data3.to_csv('joinQuant\\stock_price\\' + stock_list[i] + '.csv', encoding='gbk', index=False)
#     print(i)

# 1.3 获取股票的融资融券信息
# data4 = get_mtss(stock_list, start_date=None, end_date=d,
#                  fields=['date', 'sec_code', 'fin_value', 'fin_buy_value', 'fin_refund_value', 'sec_value',
#                          'sec_sell_value', 'sec_refund_value', 'fin_sec_value'], count=365)
# data4.to_csv('joinQuant\\stock_fin.csv', encoding='gbk', index=False)

# 1.4 股票资金流向数据
# data5 = get_money_flow(stock_list, start_date=None, end_date=d,
#                        fields=['date', 'sec_code', 'change_pct', 'net_amount_main', 'net_pct_main', 'net_amount_xl',
#                                'net_pct_xl', 'net_amount_l', 'net_pct_l', 'net_amount_m', 'net_pct_m', 'net_amount_s',
#                                'net_pct_s'], count=365)
# data5.to_csv('joinQuant\\stock_flow.csv', encoding='gbk', index=False)

# 1.6 龙虎榜数据
# data6 = get_billboard_list(stock_list=stock_list, end_date=d, count=365)
# data6.to_csv('joinQuant\\stock_billboard.csv', encoding='gbk', index=False)

# 1.7 统计沪港通、深港通和港股通前十大交易活跃股的交易状况。
# data7 = finance.run_query(
#     query(finance.STK_EL_TOP_ACTIVATE).filter(finance.STK_EL_TOP_ACTIVATE.day >= d + timedelta(-365)))
# data7.to_csv('joinQuant\\stock_top10.csv', encoding='utf_8_sig', index=False)

# 1.8 上市公司基本信息
# data8 = finance.run_query(
#     query(finance.STK_COMPANY_INFO.company_id, finance.STK_COMPANY_INFO.full_name, finance.STK_COMPANY_INFO.a_code,
#           finance.STK_COMPANY_INFO.b_code, finance.STK_COMPANY_INFO.h_code,
#           finance.STK_COMPANY_INFO.legal_representative, finance.STK_COMPANY_INFO.register_location,
#           finance.STK_COMPANY_INFO.register_capital, finance.STK_COMPANY_INFO.establish_date,
#           finance.STK_COMPANY_INFO.industry_id, finance.STK_COMPANY_INFO.industry_1,
#           finance.STK_COMPANY_INFO.industry_2, finance.STK_COMPANY_INFO.ceo))
# data8.to_csv('joinQuant\\company_basic.csv', encoding='utf_8_sig', index=False)

# 1.9 获取上市公司在公告中公布的员工情况，包括员工人数、学历等信息
# data9 = finance.run_query(
#     query(finance.STK_EMPLOYEE_INFO).filter(finance.STK_EMPLOYEE_INFO.pub_date >= d + timedelta(-365)))
# data9.to_csv('joinQuant\\company_employees.csv', encoding='utf_8_sig', index=False)

# 1.10 记录上市公司管理人员的任职情况
# data10 = finance.run_query(
#     query(finance.STK_MANAGEMENT_INFO).filter(finance.STK_MANAGEMENT_INFO.pub_date >= d + timedelta(-365),
#                                               finance.STK_MANAGEMENT_INFO.on_job == 1).order_by(
#         finance.STK_MANAGEMENT_INFO.pub_date))
# data10.to_csv('joinQuant\\company_managers.csv', encoding='utf_8_sig', index=False)

# 1.11 记录由上市公司年报、中报、一季报、三季报统计出的分红转增情况。  未分析，太复杂了
# data11 = finance.run_query(query(finance.STK_XR_XD).filter(finance.STK_XR_XD.code==code).order_by(finance.STK_XR_XD.report_date).limit(n)

# 1.12 获取上市公司前十大股东的持股情况，包括持股数量，所持股份性质，变动原因等。
# data12 = finance.run_query(
#     query(finance.STK_SHAREHOLDER_TOP10).filter(finance.STK_SHAREHOLDER_TOP10.pub_date >= d + timedelta(-365)))
# data12.to_csv('joinQuant\\stock_holder_top10.csv', encoding='utf_8_sig', index=False)

# 1.13 获取上市公司全部股东户数，A股股东、B股股东、H股股东的持股户数
# data13 = finance.run_query(query(finance.STK_HOLDER_NUM).filter(finance.STK_HOLDER_NUM.pub_date >= d + timedelta(-365)))
# data13.to_csv('joinQuant\\stock_holder_num.csv', encoding='utf_8_sig', index=False)

# 1.14 大股东增减持
# data14 = finance.run_query(query(finance.STK_SHAREHOLDERS_SHARE_CHANGE).filter(
#     finance.STK_SHAREHOLDERS_SHARE_CHANGE.pub_date >= d + timedelta(-365)))
# data14.to_csv('joinQuant\\stock_bigHolderChange.csv', encoding='utf_8_sig', index=False)

# # 1.15 上市公司股本变动
# data15 = finance.run_query(
#     query(finance.STK_CAPITAL_CHANGE).filter(finance.STK_CAPITAL_CHANGE.pub_date >= d + timedelta(-365)))
# data15.to_csv('joinQuant\\stock_baseChange.csv', encoding='utf_8_sig', index=False)
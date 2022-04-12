from __future__ import division

import os

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import base64
import io

from openpyxl import load_workbook

#导入数据
from pip._vendor.msgpack.fallback import xrange

count=1
# 设置时间范围
day=[7,15,30]
# 设置delta
dt = 1
#定义蒙特卡罗函数
def stock_monte_carlo(start_price,days,mu,sigma):
    ''' This function takes in starting stock price, days of simulation,mu,sigma, and returns simulated price array'''

    # Define a price array
    price = np.zeros(days)
    price[0] = start_price
    # Schok and Drift
    shock = np.zeros(days)
    drift = np.zeros(days)

    # Run price array for number of days
    for x in xrange(1,days):

        # Calculate Schock
        shock[x] = np.random.normal(loc=mu * dt, scale=sigma * np.sqrt(dt))
        # Calculate Drift
        drift[x] = mu * dt
        # Calculate Price
        price[x] = price[x-1] + (price[x-1] * (drift[x] + shock[x]))

    return price

path = "stock_price"
files = os.listdir(path)


for filename in files:
    StockData1 = pd.read_csv('stock_price/'+filename, index_col='date')
    StockData1.index = pd.to_datetime(StockData1.index)
    StockData1['Return'] = StockData1['close'].pct_change(1)
    StockData1 = StockData1.dropna()
    ret_df = pd.concat([StockData1['Return']], axis=1)
    filename=filename[0:11]
    ret_df.columns = [filename]
    ret_df.head()

    #  ---模拟风险价值---

    # 过去一年日收益率平均值
    mu = ret_df.loc['2019-11-27':'2021-5-28'].mean()[filename]

    # 过去一年日收益率标准差
    sigma = ret_df.std()[filename]

    start_price = StockData1['close'][-1]

    # 设置模拟次数
    runs = 5000

    # 创建一个空矩阵来保存最终价格数据
    simulations = np.zeros(runs)

    # 将numpy的打印选项设置为仅显示数组中的0-5个点以抑制输出
    np.set_printoptions(threshold=5)

    list=[filename]
    for days in day:
        for run in xrange(runs):
            # 将模拟数据点设置为该运行的最后一个股价
            simulations[run] = stock_monte_carlo(start_price, days, mu, sigma)[days - 1];
        # define q as the 1% empirical qunatile, this basically means that 99% of the values should fall between here
        q = np.percentile(simulations, 1)

        # 绘制价格分布图
        plt.hist(simulations, bins=200)

        # Starting Price、Mean ending price、Variance of the price (within 99% confidence interval)、显示1%分位数
        plt.figtext(0.6, 0.8, s="Start price: $%.2f" % start_price)
        plt.figtext(0.6, 0.7, "Mean final price: $%.2f" % simulations.mean())
        plt.figtext(0.6, 0.6, "VaR(0.99): $%.2f" % (start_price - q,))
        plt.figtext(0.15, 0.6, "q(0.99): $%.2f" % q)

        # 在1%分位数结果处绘制一条线
        plt.axvline(x=q, linewidth=4, color='r')

        # 标题
        plt.title(u"Final price distribution for Stock " + filename + " after %s days" % days, weight='bold');

        # 图片1转为base64
        pic_IObytes = io.BytesIO()
        plt.savefig(pic_IObytes, format='png')
        pic_IObytes.seek(0)
        pic_Res1 = base64.b64encode(pic_IObytes.read())

        list.append(simulations.mean())
        list.append(start_price - q)
        list.append(q)
        list.append(pic_Res1)
        # print(pic_Res1)
    work = load_workbook(filename="../../../../../../../课程/软件工程与计算III/迭代三/gzj/Stock.xlsx")
    sheet = work.active  # 默认为第一张表格
    sheet.append(list)
    count=count+1
    work.save(filename="../../../../../../../课程/软件工程与计算III/迭代三/gzj/Stock.xlsx")
    print(count)







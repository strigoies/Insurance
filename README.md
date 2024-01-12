# 基于隐语义模型的保险推荐系统-算法和后端

本平台推荐算法采用Spark技术、业务系统使用Java Web技术，使用Scala和Java语言开发，数据库使用MongDB和ES存储数据。

## 保险推荐接口

组路由为/BusinessServer

请求样例：

![img](https://cdn.nlark.com/yuque/0/2023/png/33957085/1700397253130-a60007cc-bc0c-47e2-bafa-106f4be0a4f3.png)

### 一、热门推荐	



接口描述：获取最热保险（根据点击量）



路由：GET /rest/insurance/hot



所用表：RateMoreRecentlyMovies



参数（form-data）：

| 字段名 | 类型   | 描述     |
| ------ | ------ | -------- |
| num    | number | 获取数量 |



返回参数：



```plain
{
    "success": true,
    "insurances": [
        {
            "mid": 1887,
            "name": "长城人寿保险股份有限公司",
            "descri": "长城金惠利B款两全保险（分红型）",
            "issue": "2023-10-12",
            "genres": "人寿保险",
            "price": 2914.01,
            "url": "http://www.iachina.cn/IC/tkk/01/55547a58-624a-415f-bf1f-eaf684400402.html",
            "status": "停售",
            "age": "18周岁-55周岁",
            "period": "20年",
            "scope": "身故或全残保险金 重大疾病保险金，特定疾病额外给付保险金 保险费豁免",
            "count": 2682
        }
    ]
}
```



### 二、最新推荐



接口描述：返回最新的保险



路由：GET /rest/insurance/new



所用表：Movie



参数（form-data）：

| 字段名 | 类型   | 描述     |
| ------ | ------ | -------- |
| num    | number | 获取数量 |



返回参数：



```plain
{
    "success": true,
    "insurances": [
        {
            "mid": 3214,
            "name": "中国平安人寿保险股份有限公司",
            "descri": "平安金富贵两全保险（分红型，A）",
            "issue": "2023-12-31",
            "genres": "人寿保险",
            "price": 1600.77,
            "url": "http://www.iachina.cn/IC/tkk/01/0dcad129-77d3-45f2-8157-492b0f1f69b9.html",
            "status": "停售",
            "age": "18周岁-55周岁",
            "period": "20年",
            "scope": "身故或全残保险金 重大疾病保险金，特定疾病额外给付保险金 保险费豁免",
            "count": 1206
        }
    ]
}
```



### 三、推荐内容



接口描述：根据用户名获取推荐内容



路由：GET /rest/insurance/wish



所用表：



用户推荐矩阵-UserRecs



top10列表-GenresTopMovies（在用户刚注册后，会用到该表）



参数（form-data）：

| 字段名   | 类型   | 描述     |
| -------- | ------ | -------- |
| username | String | 用户名   |
| num      | number | 获取数量 |



返回参数：



```plain
{
    "success": true,
    "insurances": [
        {
            "mid": 1891,
            "name": "长城人寿保险股份有限公司",
            "descri": "长城爱相随年金保险",
            "issue": "2015-02-27",
            "genres": "人寿保险",
            "price": 1713.36,
            "url": "http://www.iachina.cn/IC/tkk/01/107fe3de-330a-49b6-a5e9-fe0e27a4eff6.html",
            "status": "在售",
            "age": "18周岁-55周岁",
            "period": "20年",
            "scope": "身故或全残保险金 重大疾病保险金，特定疾病额外给付保险金 保险费豁免",
            "count": 4635
        }
    ]
}
```



### 四、检索内容



接口描述：根据关键词检索内容



路由：POST /rest/insurance/search



所用表：es|Insurance



参数（form-data）：

```plain
{
    "query":"友邦",
    "num":1
}
```

返回参数：



```plain
{
    "success": true,
    "insurances": [
        {
            "mid": 354,
            "name": "友邦保险有限公司上海分公司（代表友邦在华各分支公司）",
            "descri": "友邦附加加惠十年期定期寿险",
            "issue": "2005-10-01",
            "genres": "人寿保险",
            "price": 3297.45,
            "url": "http://www.iachina.cn/IC/tkk/01/279af3d1-9505-44d3-bfd0-5b361459e1f3.html",
            "status": "在售",
            "age": "16周岁-65周岁",
            "period": "1年",
            "scope": "住院医疗费用补贴",
            "count": 3864
        },
        {
            "mid": 798,
            "name": "友邦保险有限公司上海分公司（代表友邦在华各分支公司）",
            "descri": "友邦附加加倍无忧定期寿险",
            "issue": "2006-01-19",
            "genres": "人寿保险",
            "price": 2940.97,
            "url": "http://www.iachina.cn/IC/tkk/01/a03d9da3-1975-423f-b48b-7b96422601f0.html",
            "status": "在售",
            "age": "28天-65周岁",
            "period": "10年、20年",
            "scope": "高额身价保障，养老、传承随心规划，一份起保，身故或全残保额逐年递增",
            "count": 492
        }
    ]
}
```



### 五、相似推荐



接口描述：在详细页面下展示同类信息



路由：POST /rest/insurance/same



所用表（form-data）：



参数：

```plain
{
    "query":"安邦",
    "num":10
}
```



返回参数：



```plain
{
    "success": false,
    "insurances": [
        {
            "mid": 3225,
            "name": "中国太平洋人寿保险股份有限公司",
            "descri": "红福多两全保险（分红型）",
            "issue": "2019-12-31",
            "genres": "人寿保险",
            "price": 3606.08,
            "url": "http://www.iachina.cn/IC/tkk/01/f2524c92-edef-407b-9aaa-6760db2d2e50.html",
            "status": "停用",
            "age": "18周岁-55周岁",
            "period": "20年",
            "scope": "身故或全残保险金 重大疾病保险金，特定疾病额外给付保险金 保险费豁免",
            "count": 4286
        },
        {
            "mid": 3230,
            "name": "中国太平洋人寿保险股份有限公司",
            "descri": "红利发两全保险（分红型）",
            "issue": "2018-01-23",
            "genres": "人寿保险",
            "price": 3291.86,
            "url": "http://www.iachina.cn/IC/tkk/01/d77b080c-42d4-4027-803a-ee8ce7a8dc6b.html",
            "status": "在售",
            "age": "18周岁-55周岁",
            "period": "20年",
            "scope": "身故或全残保险金 重大疾病保险金，特定疾病额外给付保险金 保险费豁免",
            "count": 14
        }
    ]
}
```



### 六、单个保险信息



接口描述：获取单个保险信息



路由：GET /rest/insurance/info/{id}



所用表（form-data）：



参数：

| 字段名 | 类型     | 描述   |
| ------ | -------- | ------ |
| {id}   | 路径参数 | 保险id |



返回参数：



```plain
{
    "insurance": {
        "mid": 1906,
        "name": "长生人寿保险有限公司",
        "descri": "长生瑞丰两全保险（分红型）",
        "issue": "2017-03-14",
        "genres": "人寿保险",
        "price": 3050.51,
        "url": "http://www.iachina.cn/IC/tkk/01/1ba93b0a-d577-4f9f-b35b-cf267e62448a.html",
        "status": "在售",
        "age": "18周岁-55周岁",
        "period": "20年",
        "scope": "身故或全残保险金 重大疾病保险金，特定疾病额外给付保险金 保险费豁免",
        "count": 1286
    },
    "success": true
}
```



### 七、按类别查询保险信息



接口描述：返回各类别下对应保险信息



路由：POST /rest/insurance/genres



所用表（form-data）：



参数：

```plain
{
    "sort":"人寿保险",
    "num":1
}
```

返回参数：



```plain
{
    "success": true,
    "insurances": [
        {
            "mid": 1,
            "name": "安邦人寿保险股份有限公司",
            "descri": "安邦一年团体定期寿险",
            "issue": "2013-06-24",
            "genres": "人寿保险",
            "price": 3870.71,
            "url": "http://www.iachina.cn/IC/tkk/01/f5ee769b-ed10-4cdb-93b2-e5b18cb7d024.html",
            "status": "在售",
            "age": "16周岁-65周岁",
            "period": "1年",
            "scope": "住院医疗费用补贴",
            "count": 11
        },
        {
            "mid": 1899,
            "name": "长城人寿保险股份有限公司",
            "descri": "长城赢向未来大学教育金两全保险（分红型）",
            "issue": "2007-12-09",
            "genres": "人寿保险",
            "price": 3842.99,
            "url": "http://www.iachina.cn/IC/tkk/01/6519abf8-3263-4194-819d-37c1a69c1ed7.html",
            "status": "在售",
            "age": "18周岁-55周岁",
            "period": "20年",
            "scope": "身故或全残保险金 重大疾病保险金，特定疾病额外给付保险金 保险费豁免",
            "count": 2711
        }
    ]
}
```



## 订单接口

### 八、订单管理-创建



接口描述：创建订单



路由：GET /rest/orders/create-order



所用表（form-data）：



参数：

| 字段名 | 类型 | 描述   |
| ------ | ---- | ------ |
| uid    | int  | 用户id |
| mid    | int  | 保险id |



返回参数：



```plain
{
    "success": true
}
```



### 九、用户订查询



接口描述：查看购买后的订单



路由：GET /rest/orders/find-order



所用表（form-data）：



参数：

| 字段名 | 类型 | 描述   |
| ------ | ---- | ------ |
| uid    | int  | 用户id |



返回参数：



```plain
{
    "success": true,
    "order": {
        "customer_id": 1398880209,
        "insurance_id": 1887,
        "amount": 35214.14,
        "payment_type": "支付宝",
        "status": "完成",
        "create_time": "2023-11-11 23:05:11",
        "code": "8435154151135545"
    }
}
```



### 十、订单删除

接口描述：删除订单



路由：DELETE /rest/orders/delete-order



所用表（form-data）：



参数：

| 字段名 | 类型 | 描述   |
| ------ | ---- | ------ |
| uid    | int  | 用户id |
| mid    | int  | 保险id |

返回参数：

```plain
{
    "success": true
}
```

### 十一、订单列表查询

接口描述：返回订单列表



路由：GET /rest/orders/find-orders



所用表（form-data）：



参数：

| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| offset | int  |      |
| limit  | int  |      |

返回参数：

```plain
{
    "success": true,
    "order": [
        {
            "customer_id": 1398880209,
            "insurance_id": 1887,
            "amount": 35214.14,
            "payment_type": "支付宝",
            "status": "完成",
            "create_time": "2023-11-11 23:05:11",
            "code": "8435154151135545"
        },
        {
            "customer_id": 3556498,
            "insurance_id": 1906,
            "amount": 3050.51,
            "payment_type": null,
            "status": null,
            "create_time": null,
            "code": null
        }
    ]
}
```

### 十一、订单更新

接口描述：订单更新



路由：POST /rest/orders/update-order



所用表（form-data）：



参数：

```plain
{
    "uid":3556498,
    "mid":1906,
    "newData":{
            "payment_type": "支付宝",
            "status": "完成",
            "create_time": "2023-11-11 23:05:11",
            "code": "8435154151135545"
        }
}
```

返回参数：

```plain
{
    "success": true
}
```



## 用户接口

### 十一、用户注册



接口描述：注册



路由：GET /rest/users/register



所用表（form-data）：



参数：

| 字段名   | 类型   | 描述   |
| -------- | ------ | ------ |
| username | string | 用户名 |
| password | string | 密码   |



返回参数：



```plain
{
    "success": true
}
```

### 十二、用户登录



接口描述：登录



路由：GET /rest/users/login



所用表（form-data）：



参数：

| 字段名   | 类型   | 描述   |
| -------- | ------ | ------ |
| username | string | 用户名 |
| password | string | 密码   |



返回参数：



```plain
{
    "user": {
        "uid": 3556498,
        "username": "test",
        "password": "tst",
        "first": true,
        "timestamp": 1700266069971,
        "prefGenres": [
            "人寿保险"
        ]
    },
    "success": true
}
```

### 

### 十三、用户喜好存储

接口描述：添加用户喜好

路由：POST /rest/users/store-prefer

所用表（form-data）：

参数：

```plain
{
    "uid":123,
    "genres":["人寿保险","年金"]
}
```

返回参数：

```plain
{
    "success": true
}
```

**十四、实名信息存储**
接口描述：存储实名信息
路由：POST /rest/users/real-name
所用表（form-data）：Authentication
参数：

```plain
{
    "uid":1888,
    "insuranceHolderName":"投保人姓名",
    "insuranceHolderLicenseType":"投保证件类型",
    "insuranceHolderIdNumber":"投保证件号",
    "insuranceHolderPhoneNumber":"投保手机号",
    "insuranceHolderIssue":"投保手机号",
    "insuranceHolderRemark":"备注",
    "insuranceExceptName":"被保人姓名",
    "insuranceExceptLicenseType":"被保人证件类型",
    "insuranceExceptIdNumber":"被保人证件号",
    "insuranceExceptPhoneNumber":"被保人手机号",
    "relationShip":"投保与被保人关系",
    "insuranceExceptRemark":"备注"
}
```

返回参数：

```plain
{
    "success": true
}
```

### 十五、用户列表展示

接口描述：批量查找用户信息
路由：GET /rest/users/find-users
所用表（form-data）：User
参数：

| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| offset | int  |      |
| limit  | int  |      |

返回参数：

```json
{
    "success": true,
    "order": [
        {
            "uid": -1398880209,
            "username": "yanying",
            "password": "123",
            "first": true,
            "timestamp": 1698029662417,
            "prefGenres": []
        }
    ]
}
```

### 十六、用户修改

接口描述：修改用户信息
路由：POST /rest/users/update-user
所用表（form-data）：User
参数：

```plain
{
    "uid": 48690,
    "newUser":{
        "username":"1234"
    }
}
```

返回参数：

```plain
User updated successfully
```

### 十七、用户删除

接口描述：删除用户
路由：DELETE /rest/users/delete-user
所用表（form-data）：User
参数：

| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| uid    | int  |      |

返回参数：

```plain
{
    "success": true
}
```

### 十八、单个用户信息获取

接口描述：查找单个用户信息
路由：GET /rest/users/find-user
所用表（form-data）：User
参数：

| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| uid    | int  |      |

返回参数：

```json
{
    "success": true,
    "order": {
        "uid": 3556498,
        "username": "test",
        "password": "tst",
        "first": false,
        "timestamp": 1700266069971,
        "prefGenres": [
            "人寿保险",
            "平安保险",
            "医疗险"
        ]
    }
}
```

# 数据可视化平台路由：

## 一、数据大屏

### 1.1 保险种类销售数量 

接口描述：保险种类销售数量 
路由：GET /eduservice/teacher/EveryInsurance  
所用表（form-data）：insuranceData

返回参数：

```json
{
	"code":200,
	"message":"成功",
	"EveryInsurance":[
		{
			"name": "天天保",
			"plan": "方案一",
			"sales": 14, //销售额
			"claims":1, //出险数
			"year":"2023" //年份
		},
		...
		{
			"name": "全球保",
			"plan": "方案一",
			"sales": 14,
			"claims":1,
			"year":"2023"
		},
	]
}
```

### 1.2 各省分布 

接口描述：保险种类销售数量 
路由：GET /eduservice/teacher/EveryProvinceInjury   
所用表（form-data）：provinceData

返回参数：

```json
{
	"code":200,
	"message":"成功",
	"EveryProvinceInjury":[
		{
			"name": "西藏",//注意不要带省、市
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		},
		...
		{
			"name": "黑龙江",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		}
	]
}
```

### 1.3 各月销售保险数量 

接口描述：保险种类销售数量 
路由：GET /eduservice/teacher/EveryMonthBuyInsurance   
所用表（form-data）：monthlyData

返回参数：

```json
{
	"code":200,
	"message":"成功",
	"EveryMonthBuyInsurance":[
		{
			"name": "1月",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		},
		...
		{
			"name": "12月",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		}
	]
}
```

### 1.4 各月出险数量 

接口描述：保险种类销售数量 
路由：GET /eduservice/teacher/EveryMonthInjury   
所用表（form-data）：monthlyData

返回参数：

```json
{
	"code":200,
	"message":"成功",
	"EveryMonthInjury":[
		{
			"name": "1月",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		},
		...
		{
			"name": "12月",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		}
	]
}
```

### 1.5 各行业出险数量 

接口描述：保险种类销售数量 
路由：GET /eduservice/teacher/EveryAvatarInjury   
所用表（form-data）：industryData

返回参数：

```json
{
	"code":200,
	"message":"成功",
		"EveryAvatarInjury":[
		{
			"name": "一般职业",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		},
		...
		{
			"name": "金融、服务业",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		}
	]
}
```

### 1.6 各行业购买保险数量  

接口描述：保险种类销售数量 
路由：GET /eduservice/teacher/EveryAvatarBuyInsurance   
所用表（form-data）：industryData

返回参数：

```json
{
	"code":200,
	"message":"成功",
	"EveryAvatarBuyInsurance":[
		{
			"name": "一般职业",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		},
		...
		{
			"name": "金融、服务业",
			"sales": 14, 
			"claims":1, 
			"year":"2023" 
		}
	]
}
```

## 二、销售数据

###  2.3 保险种类个方案销售占比  

接口描述：保险种类销售数量 
路由：GET /eduservice/teacher/EveryInsurancePlan   
所用表（form-data）：insuranceData

返回参数：

```json
{
	"code":200,
	"message":"成功",
	"EveryInsurancePlan":[
		{
			"name": "天天保",
			"plan": "方案一",
			"sales": 14, 
			"claims":1, 
			"year":"2023"
		},
		{
			"name": "天天保",
			"plan": "方案二",
			"sales": 14,
			"claims":1,
			"year":"2023"
		},
		...
		{
			"name": "雇主保",
			"plan": "方案一",
			"sales": 14,
			"claims":1,
			"year":"2023"
		},
		{
			"name": "雇主保",
			"plan": "方案二",
			"sales": 14,
			"claims":1,
			"year":"2023"
		},
	]
}
```

## 三、客户数据

### 3.1 行业分布

接口描述：查询各行业的保险数量
路由：**POST** /eduservice/teacher/EveryUserAvatar
所用表（form-data）：EveryUserAvatar
参数：![img](https://cdn.nlark.com/yuque/0/2023/png/40389681/1702782664797-b0e1df62-cacc-4a43-b276-5f67ade2b668.png)

| 字段名    | 类型   | 描述       |
| --------- | ------ | ---------- |
| insurance | String | 保险的名字 |

返回参数

```json
{
    "message": "success",
    "EveryUserAvatarList": [
        {
            "insurance": "一般职业",
            "value": 264
        },
        {
            "insurance": "农牧业",
            "value": 166
        },
					...
					...
        {
            "insurance": "金融、服务业",
            "value": 441
        },
        {
            "insurance": "零售批发业",
            "value": 338
        },
        {
            "insurance": "餐饮旅游业",
            "value": 449
        }
    ],
    "code": 200
}
```

### 3.2 年龄分布

接口描述：查询保险的年龄分布
路由：**POST**  /eduservice/teacher/EveryUserAge
所用表（form-data）：EveryUserAge
参数

![img](https://cdn.nlark.com/yuque/0/2023/png/40389681/1702782695570-5c4d8225-adab-4339-869e-00db3b87da76.png)

| 字段名    | 类型   | 描述       |
| --------- | ------ | ---------- |
| insurance | String | 保险的名字 |

返回参数

```json
{
    "message": "success",
    "EveryUserAgeList": [
        {
            "name": "50以上",
            "value": 1450
        },
        {
            "name": "10-20",
            "value": 599
        },
        {
            "name": "30-40",
            "value": 2305
        },
        {
            "name": "0-10",
            "value": 124
        },
        {
            "name": "20-30",
            "value": 1269
        }
    ],
    "code": 200
}
```

### 3.3性别分布

接口描述：查询保险的性别分布
路由：**POST**  /eduservice/teacher/EveryUserGender
所用表（form-data）：EveryUserGender
参数：

![img](https://cdn.nlark.com/yuque/0/2023/png/40389681/1702782719708-2344178f-3bc9-4510-a3b7-f00a458bd92f.png)

| 字段名    | 类型   | 描述       |
| --------- | ------ | ---------- |
| insurance | String | 保险的名字 |

返回参数

```json
{
    "message": "success",
    "EveryUserGenderList": [
        {
            "name": "男",
            "value": 12
        },
        {
            "name": "女",
            "value": 123
        }
    ],
    "code": 200
}
```

### 3.4 伤情分布

接口描述：查询保险的伤情分布
路由：**POST**  /eduservice/teacher/EveryUserInjury
所用表（form-data）：EveryUserInjury
参数：

![img](https://cdn.nlark.com/yuque/0/2023/png/40389681/1702782740815-09f739ec-bba0-4305-92d9-cb2b4f9386d9.png)

| 字段名    | 类型   | 描述       |
| --------- | ------ | ---------- |
| insurance | String | 保险的名字 |

返回参数

```json
{
    "message": "success",
    "EveryUserInjuryList": [
        {
            "name": "1-3类",
            "value": 12
        },
        {
            "name": "4类",
            "value": 123
        },
        {
            "name": "5类",
            "value": 456
        },
        {
            "name": "6类",
            "value": 789
        }
    ],
    "code": 200
}
```



## 四、理赔数据

![img](https://cdn.nlark.com/yuque/0/2023/png/40389681/1702643479829-7170572e-f50b-449f-a425-15c0e99b9728.png)

### 4.1 各行业理赔保险数量

接口描述：查询所有行业的理赔数据
路由：get /eduservice/teacher/EveryAvatarInjuryAll
所用表（form-data）：ClaimSettlement

返回参数

```json
{
    "code": 200,
    "message": "success",
    "EveryAvatarInjuryAll": [
        {
            "name": "一般职业",
            "value": 259
        },
        {
            "name": "农牧业",
            "value": 485
        },
        {
            "name": "出版广告业",
            "value": 761
        },
        {
            "name": "制造、维修业",
            "value": 204
        },
				...
     		...
        {
            "name": "零售批发业",
            "value": 802
        },
        {
            "name": "餐饮旅游业",
            "value": 183
        }
    ]
}
```

### 4.2 各类保险理赔与销售占比

接口描述：查询各类保险理赔与销售数量
路由：get /eduservice/teacher/EveryInsuranceBeInjury
所用表（form-data）：AvatarInjury

返回参数

```json
{
    "code": 200,
    "message": "success",
    "EveryInsuranceBeInjury": [
        {
            "insurance": "天天保",
            "name": "销售",
            "value": 751
        },
        {
            "insurance": "天天保",
            "name": "理赔",
            "value": 756
        },
        {
            "insurance": "全球保",
            "name": "销售",
            "value": 123
        },
        {
            "insurance": "全球保",
            "name": "理赔",
            "value": 757
        },
        {
            "insurance": "即时保",
            "name": "销售",
            "value": 123
        },
        {
            "insurance": "即时保",
            "name": "理赔",
            "value": 456
        },
        {
            "insurance": "雇主保",
            "name": "销售",
            "value": 123
        },
        {
            "insurance": "雇主保",
            "name": "理赔",
            "value": 456
        }
    ]
}
```

### 4.3 各保险种类理赔数量

接口描述：查询各保险种类理赔数量
路由：get /eduservice/teacher/EveryInjuryInsurance
所用表（form-data）：EveryInjuryInsurance

返回参数

```json
{
    "code": 200,
    "message": "success",
    "EveryInjuryInsurance": [
        {
            "name": "天天保",
            "value": 354
        },
        {
            "name": "全球保",
            "value": 403
        },
        {
            "name": "即时保",
            "value": 843
        },
        {
            "name": "雇主保",
            "value": 725
        },
        {
            "name": "灵工小保",
            "value": 401
        }
    ]
}
```











### 表名

#### User【用户表】

| 字段名    | 字段类型     | 字段描述           | 字段备注 |
| --------- | ------------ | ------------------ | -------- |
| uid       | int          | 用户id             |          |
| username  | string       | 用户名             |          |
| password  | string       | 用户密码           |          |
| first     | boolean      | 是否第一次登录     |          |
| genres    | List<String> | 用户偏爱的保险类型 |          |
| timestamp | string       | 用户创建时间       |          |

#### Insurance【保险数据表】

| 字段名 | 字段类型 | 字段描述     | 字段备注         |
| ------ | -------- | ------------ | ---------------- |
| mid    | int      | 保险id       |                  |
| name   | string   | 保险公司名称 |                  |
| descri | string   | 产品名称     |                  |
| issue  | string   | 发布时间     |                  |
| genres | string   | 所属类别     |                  |
| price  | double   | 价格         |                  |
| url    | string   | 详情链接     |                  |
| status | string   | 销售状态     |                  |
| age    | string   | 承保年龄     |                  |
| period | string   | 周期         |                  |
| scope  | string   | 承保范围     |                  |
| count  | int      | 点击率       | 该字段用于算法中 |



#### RateMoreRecentlyInsurances【最近保险点击量统计表】

| 字段名 | 字段类型 | 字段描述 | 字段备注 |
| ------ | -------- | -------- | -------- |
| mid    | int      | 保险id   |          |
| count  | int      | 点击量   |          |

#### 

#### UserRecs【用户推荐矩阵】

| 字段名 | 字段类型                   | 字段描述               | 字段备注 |
| ------ | -------------------------- | ---------------------- | -------- |
| uid    | int                        | 用户的id               |          |
| recs   | Array[(mid:Int,count:Int)] | 推荐给该用户的保险集合 |          |



#### GenresTopInsurances【保险类型 TOP10】

| 字段名 | 字段类型         | 字段描述  | 字段备注 |
| ------ | ---------------- | --------- | -------- |
| genres | string           | 保险类型  |          |
| recs   | Array[(mid:Int)] | Top10保险 |          |



#### Order【订单表】

| 字段名       | 字段类型 | 字段描述     | 字段备注 |
| ------------ | -------- | ------------ | -------- |
| code         | string   | 订单流水号   |          |
| customer_id  | int      | 客户uid      |          |
| insurance_id | int      | 产品id       |          |
| amount       | double   | 订单总价格   |          |
| payment_type | string   | 订单支付类型 |          |
| status       | string   | 订单支付状态 |          |
| create_time  | string   | 订单创建时间 |          |



#### Authentication【实名表】

| 字段名                     | 字段类型 | 字段描述           | 字段备注 |
| -------------------------- | -------- | ------------------ | -------- |
| uid                        | int      | 用户id             |          |
| insuranceHolderName        | string   | 投保人姓名         |          |
| insuranceHolderLicenseType | string   | 投保人证件类型     |          |
| insuranceHolderIdNumber    | string   | 投保人证件号       |          |
| insuranceHolderPhoneNumber | string   | 投保人手机号       |          |
| insuranceHolderIssue       | string   | 投保时间           |          |
| insuranceHolderRemark      | string   | 备注               |          |
| insuranceExceptName        | string   | 被保人姓名         |          |
| insuranceExceptLicenseType | string   | 被保人证件类型     |          |
| insuranceExceptIdNumber    | string   | 被保人证件号       |          |
| insuranceExceptPhoneNumber | string   | 被保人手机号       |          |
| relationShip               | string   | 投保人与被保人关系 |          |
| insuranceExceptRemark      | string   | 备注               |          |

#### EveryUserAvatar【行业分布】

| 字段名    | 字段类型 | 字段描述 | 字段备注 |
| --------- | -------- | -------- | -------- |
| Insurance | String   | 行业名称 |          |
| name      | String   | 保险名称 |          |
| value     | int      | 保险数量 |          |

#### EveryUserGender【性别分布】

| 字段名    | 字段类型 | 字段描述     | 字段备注 |
| --------- | -------- | ------------ | -------- |
| Insurance | String   | 保险名称     |          |
| name      | String   | 性别         |          |
| value     | int      | 销售保险数量 |          |

#### EveryUserAge【年龄分布】

| 字段名    | 字段类型 | 字段描述     | 字段备注 |
| --------- | -------- | ------------ | -------- |
| Insurance | String   | 保险名称     |          |
| name      | String   | 年龄分布     |          |
| value     | int      | 销售保险数量 |          |

#### EveryUserInjury【伤情分布】

| 字段名    | 字段类型 | 字段描述     | 字段备注 |
| --------- | -------- | ------------ | -------- |
| Insurance | String   | 保险名称     |          |
| name      | String   | 伤情等级     |          |
| value     | int      | 销售保险数量 |          |

#### ClaimSettlement【各行业保险理赔数量】

| 字段名    | 字段类型 | 字段描述           | 字段备注 |
| --------- | -------- | ------------------ | -------- |
| Insurance | String   | 行业名称           |          |
| number    | int      | 该行业理赔保险数量 |          |

#### AvatarInjury【类保险理赔与销售占比】

| 字段名    | 字段类型 | 字段描述     | 字段备注 |
| --------- | -------- | ------------ | -------- |
| Insurance | String   | 保险名称     |          |
| name      | String   | 理赔还是销售 |          |
| value     | int      | 该种类的数量 |          |

#### EveryInjuryInsurance【各保险种类理赔数量】

| 字段名    | 字段类型 | 字段描述     | 字段备注 |
| --------- | -------- | ------------ | -------- |
| Insurance | String   | 保险种类名称 |          |
| value     | int      | 该种类的数量 |          |

### 暂时没用到的表：

#### AverageInsurances【保险平均评分表】

| 字段名 | 字段类型 | 字段描述 | 字段备注 |
| ------ | -------- | -------- | -------- |
| mid    | int      | 保险id   |          |
| avg    | double   | 平均分   |          |



#### InsuranceRecs【保险相似性矩阵】

| 字段名 | 字段类型                      | 字段描述             | 字段备注 |
| ------ | ----------------------------- | -------------------- | -------- |
| mid    | int                           | 保险id               |          |
| recs   | Array[(mid:Int,socre:Double)] | 该保险相似的保险集合 |          |

#### RateMoreInsurances【保险评分个数统计表】

| 字段名 | 字段类型 | 字段描述 | 字段备注 |
| ------ | -------- | -------- | -------- |
| mid    | int      | 保险id   |          |
| count  | int      | 评分数   |          |

#### 





##接口格式

####例如：
http://lintianranqi.com/lift/v1/users.json

####其中：
•lintianranqi.com是域名；
•v1为版本号；
•users.json为资源路径(Endpoint)；

####返回格式：
{
    "flag": "success",
    "msg": "",
    "data": null
}

"flag": 成功失败标识符（error和success）
"msg" : 成功失败文字信息
"data": 返回数据


####例:
{
    "flag": "success",
    "msg": "",
    "data": {
        "name": "",
        "lift": {
            "id": "",
            "number": ""
        }
    }
}

##API接口
<h3 id='登录方法'>登录方法</h3>

	POST  /login.json

| 名称 			| 类型		| 描述    
| ------------ 	| ----  	|-----      
| username		|String		| 用户名      
| password   	|String		| 密码  

[Staff](#Staff)

<h3 id='修改密码'>修改密码</h3>

	POST  staffs/:id/password.json

| 名称 			| 类型		| 描述     
| ------------ 	| ----  	|-----      
| password		|String		| 原密码  	
| new_password  |String 	| 新密码
| new_password2 |String 	| 新密码

null

<h3 id='获取抢修单列表'>获取抢修单列表</h3>

	GET  /faults.json

| 名称 			| 类型		| 描述     
| ------------ 	|----		| ----      
| staff_id		| String	| 员工id，登录者id    
 

List<[Fault](#Fault)>

<h3 id='获取抢修单列表'>获取抢修单列表</h3>

	GET  /faults.json

| 名称 			| 类型		| 描述     
| ------------ 	|---		| ----      
| staff_id		|String		| 员工id，登录者id    
| type		   	|			| 1  
| last_fault_id	|String		| 本页最后一条抢修单  

List<[Fault](#Fault)>

<h3 id='扫描确定为故障电梯'>扫描确定为故障电梯</h3>

	GET  /faults/:id/scan.json

| 名称 			| 类型  		| 描述     
| ------------ 	| ----  	|--    
| code			|String		| 电梯编码，扫描  

[Fault](#Fault)

<h3 id='抢修签到'>~~抢修签到~~</h3>

	POST  /faults/:id/sign.json

| 名称 			| 描述     
| ------------ 	| ----      

null


<h3 id='获取故障短语'>获取故障短语</h3>

	GET  /language.json

| 名称 			| 类型	| 描述     
| ------------ 	| ----  |---     
| fault_id		| String| 抢修单id

[Language](#Language)

<h3 id='上传图片'>上传图片</h3>

	POST  /base64.json

| 名称 			| 类型	| 描述     
| ------------ 	| --- 	| ----      
| img		    |String	|BASE64

[Image](#Image)

<h3 id='上传抢修信息'>上传抢修信息</h3>

	PUT  /faults/:id.json

| 名称 			| 类型		| 描述     
| ------------ 	|----  		| ----      
| b_img[]		   	|String[] 	| 维修前照片(图片code,img[0],img[1],img[2])
| e_img[]	  	|String[]	| 维修后照片(图片code,e_img[0],e_img[1],e_img[2])
| error_describe|String		| 故障常用语
| parts			|bool		|是否更换配件0否，1是
| parts_name	|String		| 配件名字
| cost			|double		| 维修金额


null

<h3 id='历史抢修信息'>历史抢修信息</h3>

	GET  /faults/:id.json

| 名称 			| 类型		| 描述     
| ------------ 	|----  		| ----      


[Fault](#Fault)

<h3 id='获取维保单列表'>获取维保单列表</h3>

	GET  /maintenances.json

| 名称 			| 类型		| 描述     
| ------------ 	|----  		| ----
|staff_id		| String	| 员工id，登录者id     


[Maintenance](#Maintenance)

<h3 id='获取维保单列表'>获取维保单列表</h3>

	GET  /maintenances.json

| 名称 			| 类型		| 描述     
| ------------ 	|---		| ----      
| staff_id		|String		| 员工id，登录者id    
| type		   	|			| 1  
| last_fault_id	|String		| 本页最后一条抢修单  

List<[Maintenance](#Maintenance)>


<h3 id='扫描确定为维保电梯'>扫描确定为维保电梯</h3>

	GET  /maintenances/:id/scan.json

| 名称 			| 类型  		| 描述     
| ------------ 	| ----  	|--    
| code			|String		| 电梯编码，扫描  

List<[Maintenance](#Maintenance)>

<h3 id='抢修签到'>~~抢修签到~~</h3>

	POST  /maintenances/:id/sign.json

| 名称 			| 描述     
| ------------ 	| ----      

null

数据类型
=====
<h3 id='Staff'>Staff fields</h3>

| 名称 				| 类型		| 描述  	
| ------------ 		| ----		| ---	
| staff_id			| String	| 员工id
| name			| String	| 姓名
| phone			| String	| 电话
| department		| String| 部门
| company			| String	| 公司名

<h3 id='Fault'>Fault fields</h3>

| 名称 				| 类型		| 描述  	
| ------------ 		| ----		| ---	
| id			    | String	| 抢修单id
| number		    | String	| 抢修单号
| error_type		| String    | 故障类型
| error_desc		| String	| 故障描述短语
| is_parts			| bool		| 是否更换配件
| parts_name 		| String  	| 配件名字
| cost				| double	| 维修金额
| end_time			| double	| 维修时间
| b_img				| List<String>	|维修前的照片
| e_img				| List<String>	|维修后的照片
| lift			    | [Lift](#Lift)	| 电梯
| status			| String	| 抢修单状态（2：等待处理，3：已经签到正在处理，4：抢修完成 ）

<h3 id='Lift'>Lift fields</h3>

| 名称 				| 类型		| 描述  	
| ------------ 		| ----		| ---	
| id			    | String	| 电梯id
| fault_id			| String	| 抢修单id
| code		 		| String	| 电梯编号
| brand				| String    | 电梯品牌
| address			| String	| 电梯地址
| customer			| String	| 物业公司
| company			| String	| 维保公司
| customer_phone	| String	| 物业公司电话


<h3 id='Language'>Language fields</h3>

| 名称 				| 类型		| 描述  	
| ------------ 		| ----		| ---	
| shortname			| String	| 故障短语
| solution			| String	| 简单解决方法

<h3 id='Image'>Image fields</h3>

| 名称 				| 类型		| 描述  	
| ------------ 		| ----		| ---	
| url				| String	| 图片链接
| code				|	 String	| 标识码

<h3 id='Maintenance'>Maintenance fields</h3>

| 名称 				| 类型		| 描述  	
| ------------ 		| ----		| ---	
| id			    | String	| 维保单id
| number		    | String	| 单号
| lift			    | [Lift](#Lift)	| 电梯
| status			| String	| 状态（0：待维保，1维保中，2维保完成，3过期 ）
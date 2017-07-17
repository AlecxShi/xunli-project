CREATE TABLE COMMON_USER_INFO(
    id INT AUTO_INCREMENT PRIMARY KEY  ,    --注册用户编号，唯一
    usertype INT,           --注册用户类型,分为系统、机器、普通用户
    phone CHAR(11),              --注册手机号
    password VARCHAR(32),           --登录密码
    location    VARCHAR(255),           --当前所在地,获取地理信息而得
    username    VARCHAR(255),           --用户名称
    excit   INT,              --激励标记
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         --创建日期
    last_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP   --最后修改时间
)
INSERT INTO COMMON_USER_INFO(usertype,phone,username,password,location,excit) VALUES(1,'15000591868','shihj','shihj','SH',2);


CREATE TABLE CHILDREN_BASE_INFO(
    id,                 //注册用户子女信息编号
    parentId,           //所属注册用户
    gender,             //性别
    bornLocation,       //出生地
    currentLocation,    //当前居住地
    age,                //年龄
    education,          //教育程度
    score,              //当前评分
)

CREATE TABLE CHILDREN_EXTEND_INFO(
    id,                 //子女扩展信息1的标号
    childrenId,         //子女编号
    photo,              //照片信息
    profession,         //职业信息
    income,             //收入情况
    height,             //身高
    weight,             //体重
    car,                //是否有车
    house               //是否有房
)

CREATE TABLE CHILDREN_EXTEND_INFO1(
    id,                 //扩展信息2编号
    childrenId,         //子女编号
    company,            //所在公司
    position,           //当前职位
    school,             //毕业学校

)
CREATE TABLE common_user_info(
    id BIGINT AUTO_INCREMENT PRIMARY KEY  ,                  --注册用户编号，唯一
    usertype INT,                                            --注册用户类型,分为系统、机器、普通用户
    phone CHAR(11),                                          --注册手机号
    password VARCHAR(32),                                    --登录密码
    location    VARCHAR(255),                                --当前所在地,获取地理信息而得
    username    VARCHAR(255),                                --用户名称
    excit   INT DEFAULT 0,                                   --激励标记
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         --创建日期
    last_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP     --最后修改时间
);

CREATE TABLE children_info(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,           --注册用户子女信息编号
    name VARCHAR(255),                                       --显示用户名
    parentId BIGINT NOT NULL,                                --所属注册用户
    gender BIGINT NOT NULL,                                  --性别
    bornLocation VARCHAR(255),                               --出生地
    currentLocation VARCHAR(255),                            --当前居住地
    birthday VARCHAR(12),                                    --年龄
    height INT,                                              --身高
    education BIGINT,                                        --教育程度,字典匹配
    company BIGINT,                                          --所在公司,字典匹配
    profession VARCHAR(255),                                 --职业信息
    position    VARCHAR(255),                                --当前职位
    school_type BIGINT,                                      --学校类型,字典匹配
    school  VARCHAR(255),                                    --毕业学校
    income BIGINT,                                           --收入情况,字典匹配
    car BIT,                                                 --是否有车
    house BIGINT,                                            --是否有房,字典匹配
    photo VARCHAR(255),                                      --照片信息,照片存储路径
    hobby VARCHAR(255),                                      --爱好信息,尚未确定是否选择
    more_introduce TEXT,                                     --更多自我介绍
    score INT,                                               --当前评分
    label VARCHAR(255),                                      --标签
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         --创建日期
    last_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP     --最后修改时间
)

CREATE TABLE children_base_info(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,           --注册用户子女信息编号
    name VARCHAR(255),                                       --显示用户名
    parentId BIGINT NOT NULL,                                --所属注册用户
    gender BIGINT NOT NULL,                                  --性别
    bornLocation VARCHAR(255),                               --出生地
    currentLocation VARCHAR(255),                            --当前居住地
    age INT,                                                 --年龄
    height INT,                                              --身高
    education BIGINT,                                        --教育程度
    score INT,                                               --当前评分
    label VARCHAR(255),                                      --标签
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         --创建日期
    last_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP     --最后修改时间
);

CREATE TABLE children_extend_info(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,           --子女扩展信息
    childrenId BIGINT NOT NULL,                              --子女编号
    profession VARCHAR(255),                                 --职业信息
    company VARCHAR(255),                                    --所在公司
    position    VARCHAR(255),                                --当前职位
    school  VARCHAR(255),                                    --毕业学校,
    income BIGINT,                                           --收入情况,字典匹配
    car BIT,                                                 --是否有车
    house BIGINT,                                            --是否有房,字典匹配
    photo VARCHAR(255),                                      --照片信息,照片存储路径
    hobby VARCHAR(255),                                      --爱好信息,字典组装
    score INT,                                               --扩展评分
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         --创建日期
    last_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP     --最后修改时间
);
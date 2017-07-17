CREATE TABLE DICT_INFO(
    id INT AUTO_INCREMENT PRIMARY KEY,
    dict_type VARCHAR(50),
    dict_value VARCHAR(10),
    dict_desc   VARCHAR(255)
)
INSERT INTO DICT_INFO(id,dict_type,dict_value,dict_desc) VALUES(1,'USER_TYPE','COMMON','PTYH')
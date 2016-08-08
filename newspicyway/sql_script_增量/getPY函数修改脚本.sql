SET NAMES 'utf8';
DELIMITER $$
DROP FUNCTION IF EXISTS `getPY`$$ 
CREATE FUNCTION getPY(in_string VARCHAR(1000))
  RETURNS TEXT CHARSET utf8
  SQL SECURITY INVOKER
BEGIN


DECLARE tmp_str VARCHAR(1000) CHARSET utf8 DEFAULT '' ; 

DECLARE tmp_len SMALLINT DEFAULT 0;

DECLARE tmp_char VARCHAR(2) CHARSET gbk DEFAULT '';

DECLARE tmp_rs VARCHAR(1000) CHARSET gbk DEFAULT '';

DECLARE tmp_cc VARCHAR(2) CHARSET gbk DEFAULT '';

SET tmp_str = in_string;

SET tmp_len = LENGTH(tmp_str);

WHILE tmp_len > 0 DO 

SET tmp_char = LEFT(tmp_str,1);

SET tmp_cc = tmp_char;

IF LENGTH(tmp_char)>1 THEN

SELECT ELT(INTERVAL(CONV(HEX(tmp_char),16,10),0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC 

,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA ,0xCEF4,0xD1B9,0xD4D1), 

'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','W','X','Y','Z') INTO tmp_cc; 

END IF; 

SET tmp_rs = CONCAT(tmp_rs,tmp_cc);

SET tmp_str = SUBSTRING(tmp_str,2);

SET tmp_len = LENGTH(tmp_str);

END WHILE; 

IF ISNULL(tmp_rs) THEN
RETURN '';
ELSE
RETURN tmp_rs;
END IF;

END
$$
DELIMITER ;
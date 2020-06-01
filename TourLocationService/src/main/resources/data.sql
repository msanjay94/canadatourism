use canadatourism;


INSERT INTO province (`province_id`, `province_name`) VALUES
(1, 'Alberta')
, (2, 'British Columbia')
, (3,  'Manitoba')
, (4,  'New Brunswick')
, (5,  'Newfoundland and Labrador')
, (6,  'Northwest Territories')
, (7,  'Nova Scotia')
, (8, 'Nunavut Territory')
, (9,  'Ontario')
, (10,  'Prince Edward Island')
, (11,  'Québec')
, (12,  'Saskatchewan')
, (13,  'Yukon Territory');

INSERT INTO cities (`city_id`,`city_name`,`province`) VALUES
(1,'Banff',1
),(2,'Brooks',1
),(3,'Calgary',1
),(4,'Edmonton',1
),(5,'Fort McMurray',1
),(6,'Grande Prairie',1
),(7,'Jasper',1
),(8,'Lethbridge',1
),(9,'Medicine Hat',1
),(10,'Lake Louise',1
),(11,'Red Deer',1
),(12,'Saint Albert',1
),(13,'Barkerville',2
),(14,'Burnaby',2
),(15,'Campbell River',2
),(16,'Chilliwack',2
),(17,'Courtenay',2
),(18,'Cranbrook',2
),(19,'Dawson Creek',2
),(20,'Delta',2
),(21,'Esquimalt',2
),(22,'Fort Saint James',2
),(23,'Fort Saint John',2
),(24,'Hope',2
),(25,'Kamloops',2
),(26,'Kelowna',2
),(27,'Kimberley',2
),(28,'Kitimat',2
),(29,'Langley',2
),(30,'Nanaimo',2
),(31,'Nelson',2
),(32,'New Westminster',2
),(33,'North Vancouver',2
),(34,'Oak Bay',2
),(35,'Penticton',2
),(36,'Powell River',2
),(37,'Prince George',2
),(38,'Prince Rupert',2
),(39,'Quesnel',2
),(40,'Revelstoke',2
),(41,'Rossland',2
),(42,'Trail',2
),(43,'Vancouver',2
),(44,'Vernon',2
),(45,'Victoria',2
),(46,'West Vancouver',2
),(47,'White Rock',2
),(48,'Brandon',3
),(49,'Churchill',3
),(50,'Dauphin',3
),(51,'Flin Flon',3
),(52,'Kildonan',3
),(53,'Saint Boniface',3
),(54,'Swan River',3
),(55,'Thompson',3
),(56,'Winnipeg',3
),(57,'York Factory',3
),(58,'Bathurst',4
),(59,'Caraquet',4
),(60,'Dalhousie',4
),(61,'Fredericton',4
),(62,'Miramichi',4
),(63,'Moncton',4
),(64,'Saint John',4
),(65,'Argentia',5
),(66,'Bonavista',5
),(67,'Channel-Port aux Basques',5
),(68,'Corner Brook',5
),(69,'Ferryland',5
),(70,'Gander',5
),(71,'Grand Falls–Windsor',5
),(72,'Happy Valley–Goose Bay',5
),(73,'Harbour Grace',5
),(74,'Labrador City',5
),(75,'Placentia',5
),(76,'Saint Anthony',5
),(77,'St. John’s',5
),(78,'Wabana',5
),(79,'Fort Smith',6
),(80,'Hay River',6
),(81,'Inuvik',6
),(82,'Tuktoyaktuk',6
),(83,'Yellowknife',6
),(84,'Baddeck',7
),(85,'Digby',7
),(86,'Glace Bay',7
),(87,'Halifax',7
),(88,'Liverpool',7
),(89,'Louisbourg',7
),(90,'Lunenburg',7
),(91,'Pictou',7
),(92,'Port Hawkesbury',7
),(93,'Springhill',7
),(94,'Sydney',7
),(95,'Yarmouth',7
),(96,'Iqaluit',8
),(97,'Bancroft',9
),(98,'Barrie',9
),(99,'Belleville',9
),(100,'Brampton',9
),(101,'Brantford',9
),(102,'Brockville',9
),(103,'Burlington',9
),(104,'Cambridge',9
),(105,'Chatham',9
),(106,'Chatham-Kent',9
),(107,'Cornwall',9
),(108,'Elliot Lake',9
),(109,'Etobicoke',9
),(110,'Fort Erie',9
),(111,'Fort Frances',9
),(112,'Gananoque',9
),(113,'Guelph',9
),(114,'Hamilton',9
),(115,'Iroquois Falls',9
),(116,'Kapuskasing',9
),(117,'Kawartha Lakes',9
),(118,'Kenora',9
),(119,'Kingston',9
),(120,'Kirkland Lake',9
),(121,'Kitchener',9
),(122,'Laurentian Hills',9
),(123,'London',9
),(124,'Midland',9
),(125,'Mississauga',9
),(126,'Moose Factory',9
),(127,'Moosonee',9
),(128,'Niagara Falls',9
),(129,'Niagara-on-the-Lake',9
),(130,'North Bay',9
),(131,'North York',9
),(132,'Oakville',9
),(133,'Orillia',9
),(134,'Oshawa',9
),(135,'Ottawa',9
),(136,'Parry Sound',9
),(137,'Perth',9
),(138,'Peterborough',9
),(139,'Picton',9
),(140,'Port Colborne',9
),(141,'Saint Catharines',9
),(142,'Saint Thomas',9
),(143,'Sarnia-Clearwater',9
),(144,'Sault Sainte Marie',9
),(145,'Scarborough',9
),(146,'Simcoe',9
),(147,'Stratford',9
),(148,'Sudbury',9
),(149,'Temiskaming Shores',9
),(150,'Thorold',9
),(151,'Thunder Bay',9
),(152,'Timmins',9
),(153,'Toronto',9
),(154,'Trenton',9
),(155,'Waterloo',9
),(156,'Welland',9
),(157,'West Nipissing',9
),(158,'Windsor',9
),(159,'Woodstock',9
),(160,'York',9
),(161,'Borden',10
),(162,'Cavendish',10
),(163,'Charlottetown',10
),(164,'Souris',10
),(165,'Summerside',10
),(166,'Asbestos',11
),(167,'Baie-Comeau',11
),(168,'Beloeil',11
),(169,'Cap-de-la-Madeleine',11
),(170,'Chambly',11
),(171,'Charlesbourg',11
),(172,'Châteauguay',11
),(173,'Chibougamau',11
),(174,'Côte-Saint-Luc',11
),(175,'Dorval',11
),(176,'Gaspé',11
),(177,'Gatineau',11
),(178,'Granby',11
),(179,'Havre-Saint-Pierre',11
),(180,'Hull',11
),(181,'Jonquière',11
),(182,'Kuujjuaq',11
),(183,'La Salle',11
),(184,'La Tuque',11
),(185,'Lachine',11
),(186,'Laval',11
),(187,'Lévis',11
),(188,'Longueuil',11
),(189,'Magog',11
),(190,'Matane',11
),(191,'Montreal',11
),(192,'Montréal-Nord',11
),(193,'Percé',11
),(194,'Port-Cartier',11
),(195,'Quebec',11
),(196,'Rimouski',11
),(197,'Rouyn-Noranda',11
),(198,'Saguenay',11
),(199,'Saint-Eustache',11
),(200,'Saint-Hubert',11
),(201,'Sainte-Anne-de-Beaupré',11
),(202,'Sainte-Foy',11
),(203,'Sainte-Thérèse',11
),(204,'Sept-Îles',11
),(205,'Sherbrooke',11
),(206,'Sorel-Tracy',11
),(207,'Trois-Rivières',11
),(208,'Val-d’Or',11
),(209,'Waskaganish',11
),(210,'Batoche',12
),(211,'Cumberland House',12
),(212,'Estevan',12
),(213,'Flin Flon',12
),(214,'Moose Jaw',12
),(215,'Prince Albert',12
),(216,'Regina',12
),(217,'Saskatoon',12
),(218,'Uranium City',12
),(219,'Dawson',13
),(220,'Watson Lake',13
),(221,'Whitehorse',13
),(222,'Swamilbay',6
),(223,'Cape Breton',7
),(224,'Dartmouth',7
);


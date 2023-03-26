Реализован магазин. В файле application.properties необходимо выставить настройки базы данных и секрет для системы контроля доступа. При первичном запуске в указанной БД будут созданы необходимые таблицы для работы магазина. Добавлены объекты.

API:

Регистрация пользователя : Запрос: POST /registration Body: { “username” : String, “password : String, “email” : String } Ответ: { "jwt-token": String } Выдан jwt-token в остальных запросах в заголовке Authorization в поле необходимо указать Bearer jwt-token, где jwt-token – полученная в ответе строка, для подтверждения авторизации пользователя.

Выдача нового токена: Токен выдается на 1 час далее он устаревает, необходимо получить новый и указывать в заголовке запросов его: Запрос: POST /login Body: { “username” : String, “password : String, } , где имя пользователя и пароль должны совпадать с указанными при регистрации. Ответ: { "jwt-token": String }

Информация о пользователе:

Запрос: GET /user/show Ответ: { “username” : String, “email” : String } 4. Чтобы посмотреть все активные продукты Запрос: GET /purchase/productall Ответ: { products: List product} Где { product: { “name” : String, “description”: String, “organization”: { “name” : String, “description” : String, } “coast” : float, “count” : int, “infoSale” : { products: List productName, “sale” : int, “time” : int } “review” : List { “review” : String}, “tags” : List { “tagId” : long, “tag” : String }

“tableProduct” : List {
		“characteristic” : String
		“descriptionChar” : String
		}
“ratingForUser” : double

} } 5. Чтобы купить продукт Запрос: POST /purchase/newpurchase Body: {“product” : product (описан в пункте выше)} Ответ: { “product” : product “date” : Date } Для покупки продукт должен в наличии на складе (count>0) и баланс пользователя больше цены за продукт, после покупки цена списывается с баланса и зачисляется владельцу организации за вычетом 5%. 6. Чтобы оставить комментарий(товар обязательно должен быть куплен этим пользователем): Запрос: POST /purchase/newreview Body: { “product” : product, “review” : String }

Ответ: { “product” : product, “review” : String } 7. Чтобы поставить оценку(целую от 0 до 5)(товар обязательно должен быть куплен этим пользователем): Запрос: POST /purchase/newreview Body: { “product” : product, “rating” : int }

Ответ: { “product” : product, “rating” : double (средняя оценка за этот товар) } 8. Чтобы посмотреть историю своих покупок: Запрос: GET /purchase/historypurchase

Ответ: { “historyPurchases”:{List”purchase”} ,где purchase { “product” : product, List “date” : List Data, “count” : int } 9. Чтобы вернуть продукт(если после покупки прошло менее 3 дней, деньги будут зачислены пользователю обратно, у владельца организации будут списаны за вычетом комиссии):

Запрос: POST /purchase/returnproduct Body: { “product” : product, “date” : Data, }

Ответ: { “product” : product, “rating” : double (средняя оценка за этот товар) }

Пользователь может получить свои уведомления: Запрос: Get /purchase/notification
Ответ: {“notifications” : List notification} ,где { “notification” : { “header” : String, “date” : Date, “text” : String } } 11. Пользователь может оставить заявку на организацию(название, описание, лого (до 30 Мб)), после отправки заявки всем администраторам придет уведомление по вопросу создания организации с ее именем, организация сохранится в БД с неактивным статусом: Запрос: POST /organization Body(Form-data): name: text description: text logo: file

Ответ: Response: "OK" 12. Пользователь может получить лого любой активной организации по ее названию: Запрос: GET /organization/file name: text (params) Ответ: MediaType.IMAGE_JPEG_VALUE(bite[]) 13. Владелец организации может подать заявку на новый продукт, после отправки заявки всем администраторам придет уведомление по вопросу добавления товара с его id, товар сохранится в БД с неактивным статусом: Запрос: POST /organization/addproduct { “product” : product, “name” : String (название организации, владельцем организации должен быть данный владелец) }

Ответ: Response: "OK" 14. Администратор может получить список всех продуктов(в активном и неактивном состоянии с дополнительными параметрами): Запрос: GET /admin/products Ответ: {products: List productForAdmin} ,где { productForAdmin: { “productId” : long “name” : String, “description”: String, “organization”: { “name” : String, “description” : String, } “coast” : float, “count” : int, “infoSale” : { “infoSaleId” : long, products: List productid, “sale” : int, “time” : int

“review” : List { “review”}  
, где review:{ “reviewId” : long, “userId” : long, “review”: String } “tags” : List { “tagId” : long, “tag” : String }

“tableProduct” : List {
	“characteristic” : String,
	“descriptionChar” : String
				}
	List<rating>,
,где rating: { “ratingId” : long, “userId” : long, “rating”: int } “isActive” : boolean “isDelete” : boolean } 15. Администратор может изменять параметры товара: Запрос: POST /admin/updateproduct Body: productForAdmin (как в примере выше) Ответ: productForAdmin

@echo off
echo Compiling...
javac -encoding UTF-8 -d bin -cp "bin;lib\sqlite-jdbc.jar" -source 17 -target 17 ^
  src\main\java\com\fooodie\db\Database.java ^
  src\main\java\com\fooodie\db\mapper\UserRow.java ^
  src\main\java\com\fooodie\db\AuthDao.java ^
  src\main\java\com\fooodie\db\RestaurantDao.java ^
  src\main\java\com\fooodie\db\OrderDao.java ^
  src\main\java\com\fooodie\model\User.java ^
  src\main\java\com\fooodie\model\Restaurant.java ^
  src\main\java\com\fooodie\model\MenuItem.java ^
  src\main\java\com\fooodie\model\Order.java ^
  src\main\java\com\fooodie\model\OrderItem.java ^
  src\main\java\com\fooodie\model\Delivery.java ^
  src\main\java\com\fooodie\repository\UserRepository.java ^
  src\main\java\com\fooodie\repository\RestaurantRepository.java ^
  src\main\java\com\fooodie\repository\MenuItemRepository.java ^
  src\main\java\com\fooodie\repository\OrderRepository.java ^
  src\main\java\com\fooodie\repository\DeliveryRepository.java ^
  src\main\java\com\fooodie\service\UserService.java ^
  src\main\java\com\fooodie\service\RestaurantService.java ^
  src\main\java\com\fooodie\service\MenuService.java ^
  src\main\java\com\fooodie\service\OrderService.java ^
  src\main\java\com\fooodie\service\DeliveryService.java ^
  src\main\java\com\fooodie\services\CartStore.java ^
  src\main\java\com\fooodie\services\OrderStore.java ^
  src\main\java\com\fooodie\services\InMemoryCatalog.java ^
  src\main\java\com\fooodie\web\resp\Response.java ^
  src\main\java\com\fooodie\web\req\RequestContext.java ^
  src\main\java\com\fooodie\web\session\UserSession.java ^
  src\main\java\com\fooodie\web\session\SessionManager.java ^
  src\main\java\com\fooodie\web\template\HtmlPage.java ^
  src\main\java\com\fooodie\web\middleware\RouteHandler.java ^
  src\main\java\com\fooodie\web\middleware\Router.java ^
  src\main\java\com\fooodie\web\middleware\AuthMiddleware.java ^
  src\main\java\com\fooodie\web\handlers\HomeHandler.java ^
  src\main\java\com\fooodie\web\handlers\LoginPageHandler.java ^
  src\main\java\com\fooodie\web\handlers\LoginHandler.java ^
  src\main\java\com\fooodie\web\handlers\RegisterPageHandler.java ^
  src\main\java\com\fooodie\web\handlers\RegisterHandler.java ^
  src\main\java\com\fooodie\web\handlers\LogoutHandler.java ^
  src\main\java\com\fooodie\web\handlers\RestaurantsHandler.java ^
  src\main\java\com\fooodie\web\handlers\CartAddHandler.java ^
  src\main\java\com\fooodie\web\handlers\CartRemoveHandler.java ^
  src\main\java\com\fooodie\web\handlers\CartHandler.java ^
  src\main\java\com\fooodie\web\handlers\CheckoutHandler.java ^
  src\main\java\com\fooodie\web\handlers\OrdersHandler.java ^
  src\main\java\com\fooodie\web\handlers\ProfileHandler.java ^
  src\main\java\com\fooodie\web\handlers\AdminDashboardHandler.java ^
  src\main\java\com\fooodie\web\HttpServerApp.java ^
  src\main\java\com\fooodie\FooodieApplication.java

if %ERRORLEVEL% NEQ 0 (
  echo Compilation failed!
  pause
  exit /b 1
)

echo Starting Fooodie on http://localhost:9090 ...
java -cp "bin;lib\sqlite-jdbc.jar;lib\slf4j-api.jar;lib\slf4j-simple.jar" com.fooodie.web.HttpServerApp
pause



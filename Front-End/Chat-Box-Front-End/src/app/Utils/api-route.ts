export class ApiRoutes 
{
     // BASE URL OF LOCAL SERVER
 private static BASE_URL = 'http://localhost:8080/api/v1';
 public static IMAGE_URL = 'https://res.cloudinary.com/dsazylekr/image/upload/v1706944331/';



 public static LOGIN = this.BASE_URL+'/user/login';
 public static RECEIVER_LIST=this.BASE_URL+'/user/list/'
 public static RECEIVER_BY_ID=this.BASE_URL+'/conversion/'
 public static CONVERSATION_CHEAK=this.BASE_URL+'/conversion'
 public static SEARCH = this.BASE_URL+'/user/search/'
 public static STATUS = this.BASE_URL+'/user/status/'

 public static RECEIVED ="RECEIVED";
 public static DELIVERD ="DELIVERD";
}
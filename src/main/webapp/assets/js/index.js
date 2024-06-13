
const Login = {
    template:`
    <a class="nav-link" aria-current="page" href="#" @click="Login()" >登录/注册</a>
    `,
    methods: {
        Login: function() { //登陆弹窗控制 - 打开
            MainView.openPopup('OverlayBox');
            MainView.showOverlay('loginBox');
        }
    }
}

const Logout = {
    template:`
    <a class="nav-link" aria-current="page" href="#" @click="userLogout()" >登出</a>
    `,
    methods: {
        userLogout: function() { // 登出
            window.location.href = '/JavaEEProject/logout-servlet';
        }
    }
}

const LoginBox = {
    template:`
    <div class="form_overlay_box" @click.stop>
        <!-- 表单内容 -->
        <form>
            <div class="mb-3">
                <label for="InputUsername1" class="form-label">用户名：</label>
                <input type="text" class="form-control" id="InputUsername1" aria-describedby="UsernameHelpBlock">
                <div id="UsernameHelpBlock" class="form-text">用户名暂不支持中文。</div>
            </div>
            <div class="mb-3">
                <label for="inputPassword1" class="form-label">密码：</label>
                <input type="password" id="inputPassword1" class="form-control" aria-describedby="passwordHelpBlock" autocomplete="off"> <!-- 关闭了自动填充 -->
                <div id="passwordHelpBlock" class="form-text">
                    你的密码需要8~20位字符组成，只允许允许存在特殊字符“@”、“-”、“.”。
                </div>
            </div>
            <button type="button" class="btn btn-primary" @click="userLogin()">登录</button>
            <button type="button" class="btn btn-primary" @click="toRegister()">注册</button>
        </form>
    </div>
    `,
    methods: {
        userLogin:function () {
            MainView.userLogin();
        },
        toRegister:function (){
            MainView.toRegister();
        }
    }
}

const newQuestion = {
    template:`
    <div class="form_overlay_box" @click.stop>
        <!-- 表单内容 -->
        <form>
            <div class="mb-3">
                <label for="InputTitle1" class="form-label">标题：</label>
                <input type="text" class="form-control" id="InputTitle1" v-model="title">
            </div>
            <div class="mb-3">
                <label for="Textarea1" class="form-label">简述：</label>
                <textarea class="form-control" id="Textarea1" rows="3" v-model="description"></textarea>
            </div>
            <button type="button" class="btn btn-primary" @click="toSend()">发布</button>
        </form>
    </div>
    `,
    data(){
        return{
            title: "untitled",
            description: "",
        }
    },
    methods: {
        toSend:function () {
            MainView.addQuestion(this.title, this.description);

            this.title = "untitled";
            this.description = "";
        }
    }
}

var MainView = new Vue({
    el: "#app",
    data: {
        displayButton: "isLogin",
        displayOverlay: "loginBox",
        currentDate: '',
        currentTime: ''
    },
    components: {
        'isLogin': Login,
        'isLogout': Logout,
        'loginBox': LoginBox,
        'newQuestion': newQuestion,
    },
    methods: {
        closePopup: function(popupId) { //登陆弹窗控制 - 打开
            var loginPopup = document.getElementById(popupId);
            loginPopup.style.display = 'none';
            this.testForEngineer();
        },
        openPopup: function(popupId) { //登陆弹窗控制 - 打开
            var loginPopup = document.getElementById(popupId);
            loginPopup.style.display = 'flex';
            MainView.showOverlay('newQuestion');
        },
        userLogin: function() { //用户登录
            var passwordHelper = document.getElementById('passwordHelpBlock');
            var username = document.getElementById('InputUsername1').value;
            var password = document.getElementById('inputPassword1').value;
            if (this.isValidPassword(password)) {
                passwordHelper.style.color = "";
                //VS code样式测试 需要在前面加上'http://localhost:8081'
                window.location.href = '/JavaEEProject/login-servlet?username='+username+'&password='+password;
            }else{
                passwordHelper.style.color = "red";
            }
        },
        isValidPassword: function(password){ // *判断密码是否符合要求
            // 正则表达式，匹配8~20位字母、数字以及特殊字符"@"、"-"、"."
            var regex = /^(?=.*[a-zA-Z0-9@.-])[a-zA-Z0-9@.-]{8,20}$/;

            // 使用正则表达式的 test 方法检查密码是否符合条件
            return regex.test(password);
        },
        toRegister: function() {//跳转注册
            var username = document.getElementById('InputUsername1').value;
            var password = document.getElementById('inputPassword1').value;
            window.location.href = '/JavaEEProject/register.jsp?username='+username+'&password='+password;
        },
        showOverlay: function(boxName) {
            if (this.displayButton === "isLogin"){// 若无用户
                this.displayOverlay = 'loginBox';
            }else {
                this.displayOverlay = boxName;
            }
        },
        addQuestion: function(title,description){
            console.log(title);
            console.log(description);
            window.location.href = '/JavaEEProject/questionAdd-servlet?title='+title+'&description='+description;
        },
        openURL:function (file){
            window.location.href = '/JavaEEProject/'+file;
        },
        formattedDate() {
            const date = new Date();
            const year = date.getFullYear();
            const month = (date.getMonth() + 1).toString().padStart(2, '0');
            const day = date.getDate().toString().padStart(2, '0');

            // 格式化日期为yyyy.MM.dd的格式
            const formattedDate = `${year}.${month}.${day}`;

            return formattedDate;
        },
        formattedTime() {
            const date = new Date();
            const hours = date.getHours().toString().padStart(2, '0');
            const minutes = date.getMinutes().toString().padStart(2, '0');
            const seconds = date.getSeconds().toString().padStart(2, '0');

            // 格式化时间为hh.mm.ss的格式
            const formattedTime = `${hours}:${minutes}:${seconds}`;

            return formattedTime;
        },
        testForEngineer:function() { // 顾霖轩 测试某些方法专用
            console.log("123");
        }
    },
    mounted() {
        setInterval(() => {
            this.currentDate = this.formattedDate();
            this.currentTime = this.formattedTime();
        }, 500);
    }
});

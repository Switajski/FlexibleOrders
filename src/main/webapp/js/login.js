Ext.onReady(function(){
    Ext.QuickTips.init();
    var login = new Ext.FormPanel({
        labelWidth:80,
        url:'../resources/j_spring_security_check',
        frame:true,
        title:'Please Login',
        defaultType:'textfield',
        width:300,
        height:150,
        monitorValid:true,
        items:[{
            fieldLabel:'Username',
            name:'j_username',
            allowBlank:false
        },{
            fieldLabel:'Password',

            name:'j_password',
            inputType:'password',
            allowBlank:false
        }],

        buttons:[{

            text:'Login',
            formBind: true,
            handler:function(){
            login.getForm().submit({

                method:'POST',
                success:function(){
                Ext.Msg.alert('Status', 'Login Successful!', function(btn, text){

                    if (btn == 'ok'){
                        window.location = 'main.htm';
                    }
                });

            },

            failure:function(form, action){
                if(action.failureType == 'server'){
                    obj = Ext.util.JSON.decode(action.response.responseText);

                    Ext.Msg.alert('Login Failed!', obj.errors.reason);
                }else{
                    Ext.Msg.alert('Warning!', 'Authentication server is unreachable : ' + action.response.responseText);

                }
                login.getForm().reset();
            }
            });
        }
        }]
    });
    login.render('login');
});
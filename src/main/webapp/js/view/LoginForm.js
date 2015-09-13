Ext.define('MyApp.view.LoginForm', {
	extend : 'Ext.window.Window',
	title : 'Anmelden',
	layout : 'fit',
	closable : false,
	defaults: {
        anchor: '100%'
    },
	items : [ {
		xtype : 'form',
		bodyPadding : 10,
		items : [ {
			id : 'UsernameTextfield',
			xtype : 'textfield',
			fieldLabel : 'Username',
			name : 'username',
			allowBlank : false
		}, {
			xtype : 'textfield',
			fieldLabel : 'Password',
			name : 'password',
			inputType : 'password',
			allowBlank : false
		} ]
	} ],
	buttons : [ {
		id : "loginButton",
		text : 'Login',
		formBind : true
	} ]

});

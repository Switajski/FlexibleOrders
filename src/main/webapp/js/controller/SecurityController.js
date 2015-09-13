Ext.define('MyApp.controller.SecurityController', {
	extend : 'Ext.app.Controller',
	loginUrl : '../resources/j_spring_security_check',
	logoutUrl : '../resources/j_spring_security_logout',
	id : 'SecurityController',
	loginWindow : null,
	views : [ 'LoginForm' ],
	init : function() {
		// check if already logged in
		this.control({
			'#loginButton' : {
				click : this.login
			},
			'textfield' : {
				specialkey: function(field, e) { 
		            if(e.getKey() == e.ENTER) { 
		            	this.login();
		            } 
		        } 
			}
		});
		this.startAuthentication();

	},

	afterrender : function() {
		this.showLogin();
	},

	onKeypress : function() {
		if (keyEvent.keyCode == 13) { // = Enter
			this.login();
		}
	},

	startAuthentication : function() {
		var me = this;
		Ext.Ajax.request({
			url : '/FlexibleOrders/country',
			headers : {
				'Content-Type' : 'application/json'
			},
			success : function(response, options) {
				// The GUI does not need to authenticat - it is already autheticated
				me.onSuccessfulAuthentification();
			},
			failure : function(response, options) {
				// Not authenticated - show login form
				if (response.status === 403) {
					me.showLogin();
				} else {
					Ext.Msg.alert("Anmeldung fehlgeschlagen:<br/>"
							+ response.text);
				}
			}
		});
	},

	showLogin : function() {
		Ext.getCmp('MainPanel').disable();
		this.loginWindow = Ext.create('MyApp.view.LoginForm', {
			id : "LoginForm",
			onSave : this.logIn
		});
		this.loginWindow.show();
		this.loginWindow.focus();
		Ext.getCmp('UsernameTextfield').focus();
	},

	login : function() {
		var me = this;

		form = Ext.getCmp('LoginForm').down('form').getForm();
		// if (form.isValid()) {
		if (true) {
			form.submit({
				url : '../resources/j_spring_security_check',
				success : function(response, options) {
					me.onSuccessfulAuthentification();
					Ext.getCmp('MainPanel').enable();
					me.loginWindow.close();
				},
				failure : function(response, options) {
					Ext.Msg.alert("Fehler beim login: </br>" + response);
				}
			});
		}
	},

	onSuccessfulAuthentification : function() {
		Ext.globalEvents.fireEvent('aftersuccessfulauthetification');
	}
});
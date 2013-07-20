Ext.define('MyApp.view.BestellungWindow', {
    extend: 'Ext.window.Window',
    requires: ['MyApp.view.BestellpositionGridPanel'],
    draggable:true,
    height: 400,
    id: 'BestellungWindow',
    width: 500,
    closeAction: 'hide',
    title: 'Bestellung',
    layout: {
        align: 'stretch',
        type: 'vbox',
        pack: 'start'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    id: 'BestellungForm',
                    dock: 'top',
                    bodyPadding: 10,
                    items:[
                        {
                            xtype: 'fieldset',
                            title: 'Bestellungdetails',
                            flex:1,
                            items: [
                                {
                                    xtype: 'displayfield',
                                    anchor: '100%',
                                    name: 'orderNumber',
                                    fieldLabel: 'Bestellnr'
                                },
                                {
                                    xtype: 'displayfield',
                                    anchor: '100%',
                                    name: 'customer',
                                    fieldLabel: 'Kunde'
                                },
                                {
                                    xtype: 'displayfield',
                                    anchor: '100%',
                                    name: 'status',
                                    fieldLabel: 'Status'
                                },
                                {
                                    xtype: 'displayfield',
                                    anchor: '100%',
                                    name: 'created',
                                    fieldLabel: 'Bestelldatum'
                                }
                            ]
                        }
                        
                    ]
                }, 
                {
                	xtype:'BestellpositionGrid',
                	flex:1,
                	id: 'BestellpositionGridPanel',
                	store: 'BestellpositionDataStore',
        	        dockedItems: [{
                        xtype: 'toolbar',
                        items: [{
                             id: 'ErstelleBpButton',
                             icon: 'images/add.png',
                             text: 'hinzuf&uuml;gen',
                             scope: this
                        }, {
                             id: 'DeleteBpButton',
                             icon: 'images/delete.png',
                    	     text: 'l&ouml;schen',
                                 scope: this
                        }]
                      }]
                }
            ]
        });

        me.callParent(arguments);
    }

});
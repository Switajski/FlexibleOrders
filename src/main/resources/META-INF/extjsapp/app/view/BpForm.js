Ext.define('MyApp.view.BpForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.bpform',

    requires: ['Ext.form.field.Text'],

    initComponent: function(){
        this.addEvents('create');
        Ext.apply(this, {
            activeRecord: null,
            iconCls: 'icon-user',
            frame: true,
            title: 'Bestellposition bearbeiten',
            defaultType: 'textfield',
            bodyPadding: 5,
            fieldDefaults: {
                anchor: '100%',
                labelAlign: 'right'
            },
            items: [
                    {
                    	xtype: 'combobox',
                    	anchor:'100%',
                    	name:'artikel',
                    	fieldLabel: 'Artikel',
                    	allowBlank: false,
                    	displayField: 'name',
                    	valueField: 'productNumber',
                    	enableRegEx: true,
                    	forceSelection: true,
                    	queryMode: 'local',
                    	store: 'ArtikelDataStore',
                    	tpl: Ext.create('Ext.XTemplate',
                    			'<tpl for=".">',
                    			'<div class="x-boundlist-item">{productNumber} - {name}</div>',
                    			'</tpl>'
                    	),
                    	displayTpl: Ext.create('Ext.XTemplate',
                    			'<tpl for=".">',
                    			'{productNumber} - {name}',
                    			'</tpl>'
                    	)
                    },
                    {
                    	xtype: 'numberfield',
                    	fieldLabel: 'Menge',
                    	name:'quantity',
                    	allowBlank: false,
                    	allowDecimals: false,
                    	allowExponential: false,
                    	minValue: 1
                    },
                    {
                    	xtype: 'numberfield',
                    	name: 'priceNet',
                    	fieldLabel: 'preis Netto',
                    	allowBlank:true,
                    	minValue: 1
                    },

            ],
            dockedItems: [{
                xtype: 'toolbar',
                dock: 'bottom',
                ui: 'footer',
                items: ['->', {
                    iconCls: 'icon-save',
                    itemId: 'save',
                    text: 'Speichern',
                    disabled: true,
                    scope: this,
                    handler: this.onSave
                }, {
                    iconCls: 'icon-user-add',
                    text: 'Erstelle',
                    scope: this,
                    handler: this.onCreate
                }, {
                    iconCls: 'icon-reset',
                    text: 'Reset',
                    scope: this,
                    handler: this.onReset
                }]
            }]
        });
        this.callParent();
    },

    setActiveRecord: function(record){
        this.activeRecord = record;
        if (record) {
            this.down('#save').enable();
            this.getForm().loadRecord(record);
        } else {
            this.down('#save').disable();
            this.getForm().reset();
        }
    },

    onSave: function(){
        var active = this.activeRecord,
            form = this.getForm();

        if (!active) {
            return;
        }
        if (form.isValid()) {
            form.updateRecord(active);
            this.onReset();
        }
    },

    onCreate: function(){
        var form = this.getForm();

        if (form.isValid()) {
            this.fireEvent('create', this, form.getValues());
            form.reset();
        }

    },

    onReset: function(){
        this.setActiveRecord(null);
        this.getForm().reset();
    }
});
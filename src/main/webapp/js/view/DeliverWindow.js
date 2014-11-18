Ext
		.define(
				'MyApp.view.DeliverWindow',
				{
					extend : 'MyApp.view.TransitionWindow',
					title : 'Auftrag liefern',
					itemid : 'DeliverWindow',
					alias : 'widget.DeliverWindow',
					layout : 'fit',
					defaultInvoiceNumber : 0,
					width : 500,
					record : null,
					closeAction : 'destroy',
					bottomGrid : {
						xtype : 'PositionGrid',
						dock : 'bottom',
						id : 'CreateDeliveryNotesItemGrid',
						flex : 1,
						store : 'CreateDeliveryNotesItemDataStore',
						title : "Lieferscheinpositionen",
						features : null,
						selType : 'cellmodel',
						plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
							clicksToEdit : 1
						}) ],
						features : [ {
							ftype : 'grouping',
							groupHeaderTpl : '{columnName}: {name} ({rows.length} Position{[values.rows.length > 1 ? "en" : ""]}) {[values.rows[0].created]}',
							hideGroupedHeader : false,
							startCollapsed : false
						// id: 'orderNumber'
						} ],
						columns : [
								{
									xtype : 'gridcolumn',
									dataIndex : 'product',
									text : 'Artikel',
									displayField : 'name',
									valueField : 'productNumber'
								},
								{
									xtype : 'gridcolumn',
									dataIndex : 'productName',
									flex : 1,
									text : 'Artikel Name'
								},
								{
									xtype : 'gridcolumn',
									dataIndex : 'expectedDelivery',
									width : 80,
									text : 'Liefertermin'
								},
								{
									xtype : 'gridcolumn',
									dataIndex : 'quantityLeft',
									width : 50,
									text : 'Menge',
									editor : {
										xtype : 'numberfield',
										allowBlank : false,
										minValue : 1
									}
								},
								{
									xtype : 'actioncolumn',
									width : 30,
									sortable : false,
									menuDisabled : true,
									items : [ {
										icon : '/FlexibleOrders/images/delete.png',
										tooltip : 'Position l&ouml;schen',
										scope : this,
										handler : function(grid, rowIndex) {
											Ext
													.getStore(
															'CreateDeliveryNotesItemDataStore')
													.removeAt(rowIndex);
										}
									} ]
								} ]
					},

					headerForm : {
						xtype : 'fieldset',
						title : 'Lieferschein',
						flex : 1,
						items : [ {
							xtype : 'fieldcontainer',
							layout : 'hbox',
							defaults : {
							},
							items : [ {
								xtype : 'displayfield',
								anchor : '100%',
								name : 'customerNumber',
								fieldLabel : 'Kundennummer'
							}, {
								xtype : 'displayfield',
								anchor : '100%',
								name : 'firstName',
								margins : '0 0 0 10',
								fieldLabel : 'Name',
								labelWidth : 35
							}, {
								xtype : 'displayfield',
								anchor : '100%',
								name : 'lastName',
								margins : '0 0 0 6',
								hideLabel : 'true'
							} ]
						}, {
							xtype : 'fieldcontainer',
							layout : 'hbox',
							defaults : {
								flex : 1
							},
							items : [ {
								xtype : 'datefield',
								format : 'd/m/Y',
								allowBlank : true,
								fieldLabel : 'Lieferdatum',
								name : 'created'
							}, {
								xtype : 'checkbox',
								fieldLabel : 'ignoriere abweich. Liefertermine',
								labelWidth : 185,
								margins : '0 0 0 6',
								checked : false
							} ]
						}, {
							xtype : 'fieldcontainer',
							layout : 'hbox',
							defaults : {
								flex : 1
							},
							items : [ {
								xtype : 'textfield',
								fieldLabel : 'Lieferscheinnr.',
								name : 'deliveryNotesNumber',
								id : 'deliveryNotesNumber',
								allowBlank : false
							}, {
								xtype : 'numberfield',
								fieldLabel : 'Versandkosten',
								name : 'shipment',
								allowBlank : true,
								allowDecimals : true,
								margins : '0 0 0 6',
								minValue : 0

							} ]
						}, {
							xtype : 'fieldcontainer',
							layout : 'hbox',
							defaults : {
								flex : 1
							},
							items : [ {
								xtype : 'textfield',
								fieldLabel : 'Paketnummer',
								name : 'packageNumber',
								allowBlank : true
							}, {
								xtype : 'textfield',
								fieldLabel : 'Sendungsnr.',
								name : 'trackNumber',
								margins : '0 0 0 6',
								allowBlank : true
							} ]
						} ]
					},

					initComponent : function() {
						var me = this;
						this.editing = Ext
								.create('Ext.grid.plugin.CellEditing');
						Ext.applyIf(me, {
							layout : 'anchor',
							items : [ {
								xtype : 'form',
								id : 'DeliverForm',
								bodyPadding : 10,
								items : [ this.headerForm ],

								dockedItems : [ {
									xtype : 'toolbar',
									dock : 'bottom',
									ui : 'footer',
									items : [ '->', {
										iconCls : 'icon-save',
										itemId : 'save',
										text : 'Speichern',
										disabled : false,
										scope : this,
										handler : this.onSave
									} ]
								}, this.bottomGrid ]
							} ]
						});

						me.callParent(arguments);

					},
					/**
					 * this method is to override by the using Component
					 * (usually Panel)
					 */
					updateRecord : function() {
						console.log('Override me!');
					},

					/**
					 * this method listens to the save button and is usually
					 * overridden by a panel. see {@Link MyController.deliver}
					 */
					onSave : function() {
						var active = this.activeRecord, form = this.getForm();

						if (!active) {
							return;
						}
						if (form.isValid()) {
							form.updateRecord(active);
							this.onReset();
						}
					},

					onComboboxChange : function() {
						if (record != null) {
							var combobox = Ext.ComponentQuery
									.query('invoicenumbercombobox')[0];
							this.record.data.invoiceNumber = combobox
									.getValue();
						}
					}

				});
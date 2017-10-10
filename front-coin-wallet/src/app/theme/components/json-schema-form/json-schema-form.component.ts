import { Component, OnInit, OnChanges, ViewChild, ElementRef, AfterViewInit, Input, Output, forwardRef, EventEmitter } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';


@Component({
    selector: 'json-schema-form',
    templateUrl: './json-schema-form.component.html',
    styleUrls: ['./json-schema-form.component.scss'],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => JsonSchemaFormComponent),
            multi: true
        }
    ],
})

export class JsonSchemaFormComponent implements OnInit, OnChanges, AfterViewInit {

    static loaded = false;

    @ViewChild('jsonSchemaForm') editorContent: ElementRef;

    @Input() schema: {title: "", properties: {}};
    @Input() form: Array<any> = [];
    @Input() data = {};

    @Output() onSubmit = new EventEmitter<any>();

    private properties: Array<any> = [];
    private isFormProcessed: boolean = false;
    private isSchemaProcessed: boolean = false;
    private validData = {};

    constructor () {
    }

    ngOnInit () {
    }

    ngAfterViewInit () {

    }

    ngOnChanges () {
    }

    ngOnDestroy () {
    }

    getForm () {
        return JSON.stringify(this.form);
    }

    getSchemaTitle () {
        if (this.schema != undefined && this.schema != null) {
            return this.schema.title;
        }
        return "";
    }

    public evaluateIfStatement (statement) {
        if (statement !== undefined) {
            return this.data[statement];
        }
        return true;
    }

    getProperties () {
        if (!this.isFormProcessed) {
            if (this.properties.length == 0) {
                if (this.schema != undefined && this.schema != null) {
                    for (var property in this.schema.properties) {
                        let propertyWrapper = {
                            id: property,
                            sequence: this.schema.properties[property].id,
                            value: this.schema.properties[property]
                        };
                        this.properties.push(propertyWrapper);
                    }

                    this.sortProperties();
                    this.isSchemaProcessed = true;
                }
            }

            if (this.form != undefined && this.form != null && this.isSchemaProcessed) {
                this.isFormProcessed = true;

                for (let i = 0; i < this.form.length; i++) {
                    let key = this.form[i].key;
                    for (var j = 0; j < this.properties.length; j++) {
                        let property = this.properties[j];
                        if (property.id == key) {
                            property.type = this.form[i].type;
                            property.disabled = this.form[i].disabled;
                            if (this.form[i].ifStatement != null) {
                                property.ifStatement = this.form[i].ifStatement;
                            }
                            if (this.form[i].placeholder != null) {
                                property.placeholder = this.form[i].placeholder;
                            }
                            if (this.form[i].name != null) {
                                property.name = this.form[i].name;
                            }
                            if (this.form[i].hidden != null) {
                                property.hidden = this.form[i].hidden;
                            }
                        }
                    }
                }
            }
        }

        return this.properties;
    }

    private sortProperties () {
        this.properties.sort(function (a, b) {
            return a["sequence"] - b["sequence"];
        });
    }

    private submit () {
        this.submitForm();
    }

    private submitForm () {
        this.onSubmit.emit(this.data);
    }
}
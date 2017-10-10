var b2b = (function () {

    var parentElement = {};

    var selectedCssSelector = "";
    var selectedObjects = [];

    var form = {};

    var validatorFunctions = [];


    return {
        // Return elements based on the selector
        // Return elements based on the selector in an other element
        get: function (cssSelector, element) {
            if (element != undefined) {
                //console.log("Select objects for css selector", cssSelector);
                this.selectedCssSelector = cssSelector;
                this.selectedObjects = element.querySelectorAll(cssSelector);
                this.parentElement = element;
                return this;
            } else {
                //console.log("Select objects for css selector", cssSelector);
                this.selectedCssSelector = cssSelector;
                this.selectedObjects = window.document.querySelectorAll(cssSelector);
                return this;
            }
        },

        subset: function (start, stop) {
            this.selectedObjects = Array.from(this.selectedObjects).slice(start, stop);
        },

        exists: function () {
            return this.selectedObjects != undefined
                && this.selectedObjects != null
                && this.selectedObjects.length > 0;
        },

        replaceParameterInInnerHtml: function (parameterNumber, parameterValue) {
            var objectsLength = this.selectedObjects.length;
            for (var i = 0; i < objectsLength; i++) {
                var content = this.selectedObjects[i].innerHTML;
                content = content.replace("{" + parameterNumber + "}", parameterValue);
                this.selectedObjects[i].innerHTML = content;
            }
            return this;
        },

        removeClass: function (className) {
            var objectsLength = this.selectedObjects.length;
            for (var i = 0; i < objectsLength; i++) {
                this.selectedObjects[i].classList.remove(className);
            }
            return this;
        },

        getAttribute: function (attributeName) {
            if (this.selectedObjects.length == 1) {
                var object = this.selectedObjects[0];
                return object.getAttribute(attributeName);
            }
            return undefined;
        },

        addClass: function (className) {
            var objectsLength = this.selectedObjects.length;
            for (var i = 0; i < objectsLength; i++) {
                this.selectedObjects[i].classList.add(className);
            }
            return this;
        },

        value: function () {
            if (this.selectedObjects.length == 0) {
                alert("b2b ERROR: No elements found for selector : " + this.selectedCssSelector);
            } else {
                return this.selectedObjects[0].value;
            }
        },

        selectForm: function (formId) {
            this.form = window.document.getElementById(formId);
            return this;
        },

        addValidator: function (validator) {
            this.validatorFunctions.push(validator);
            return this;
        },

        validate: function () {

        },

        submit: function (ajax) {
            // TODO use the onSubmit from the form
            if (ajax) {
                console.log("The form", this.form);
            } else {
                this.form.submit();
            }
        },

        delete: function () {
            var objectsLength = this.selectedObjects.length;
            for (var i = 0; i < objectsLength; i++) {
                if (this.selectedObjects[i].parentNode) {
                    this.selectedObjects[i].parentNode.removeChild(this.selectedObjects[i]);
                }
            }
        },

        scrollToTop: function () {
            window.scrollTo(0, 0);
        },

        addLoadEvent: function (func) {
            var oldonload = window.onload;
            if (typeof window.onload != 'function') {
                window.onload = func;
            } else {
                window.onload = function () {
                    if (oldonload) {
                        oldonload();
                    }
                    func();
                }
            }
        }
    };
})();

var validator = (function () {

    return {

        enableRealTimeValidationOnElement: function (elementId) {
            if (b2b.get("#" + elementId).exists()) {
                console.log("Adding realtime validation", elementId);
                var t = this;
                document.getElementById(elementId).onkeyup = function () {
                    console.log("Key pressed in input field", elementId);
                    t.validate(elementId);
                }
            }
        },

        validate: function (elementId) {
            this.clearValidationClasses(elementId);

            var value = b2b.get("#" + elementId).value();
            var validators = b2b.get("#" + elementId).getAttribute("data-validators").split(",");

            var isRequired = false;
            for (var k = 0; k < validators.length; k++) {
                if (validators[k].trim() == "required") {
                    isRequired = true;
                }
            }

            var valid = true;
            for (var i = 0; i < validators.length; i++) {

                // splitting on arguments counts the first parameter as well
                // Must always e one word ex. minLength-4, hence the (arguments.length > 1)
                var arguments = validators[i].trim().split("-");

                var evalString;
                if (arguments.length > 1) {
                    var argumentsString = "";
                    for (var j = 1; j < arguments.length; j++) {
                        argumentsString += "'" + arguments[j] + "'";
                        if (j < arguments.length - 1) {
                            argumentsString += ",";
                        }
                    }
                    evalString = "this." + arguments[0] + "Validator('" + value + "', '." + elementId + "', " + isRequired + ", " + argumentsString + ")";
                } else {
                    if (validators[i].trim() != "required") {
                        evalString = "this." + validators[i].trim() + "Validator('" + value + "', '." + elementId + "', " + isRequired + ")";
                    } else {
                        evalString = "this." + validators[i].trim() + "Validator('" + value + "', '." + elementId + "')";
                    }
                }

                console.log("Evaluate string", evalString);
                valid &= eval(evalString);
            }

            if (valid) {
                this.showSuccessValidationClasses(elementId);
            } else {
                this.showErrorValidationClasses(elementId);
            }

            return valid;
        },

        requiredValidator: function (value, classNamePrefix) {
            var result = value !== undefined && value !== null && value !== "";
            if (!result) {
                b2b.get(classNamePrefix + "RequiredError").removeClass("hide");
            }
            return result;
        },

        minLengthValidator: function (value, classNamePrefix, isRequired, minLength) {
            var result = true;
            var available = value !== undefined && value !== null && value !== "";
            if (!available) {
                if (isRequired) {
                    this.replaceParameterInElementInnerHtml(classNamePrefix + "MinLengthError", 1, minLength);
                    b2b.get(classNamePrefix + "MinLengthError").removeClass("hide");
                    result = false;
                }
            } else {
                if (value.length < parseInt(minLength)) {
                    this.replaceParameterInElementInnerHtml(classNamePrefix + "MinLengthError", 1, minLength);
                    b2b.get(classNamePrefix + "MinLengthError").removeClass("hide");
                    result = false;
                }
            }
            return result;
        },

        maxLengthValidator: function (value, classNamePrefix, isRequired, maxLength) {
            var result = true;
            var available = value !== undefined && value !== null && value !== "";
            if (!available) {
                if (isRequired) {
                    this.replaceParameterInElementInnerHtml(classNamePrefix + "MaxLengthError", 1, maxLength);
                    b2b.get(classNamePrefix + "MaxLengthError").removeClass("hide");
                    result = false;
                }
            } else {
                if (value.length > parseInt(maxLength)) {
                    this.replaceParameterInElementInnerHtml(classNamePrefix + "MaxLengthError", 1, maxLength);
                    b2b.get(classNamePrefix + "MaxLengthError").removeClass("hide");
                    result = false;
                }
            }
            return result;
        },

        minMaxLengthValidator: function (value, classNamePrefix, isRequired, minLength, maxLength) {
            var result = true;
            var available = value !== undefined && value !== null && value !== "";
            if (!available) {
                if (isRequired) {
                    this.replaceParameterInElementInnerHtml(classNamePrefix + "MinMaxLengthError", 1, minLength);
                    this.replaceParameterInElementInnerHtml(classNamePrefix + "MinMaxLengthError", 2, maxLength);
                    b2b.get(classNamePrefix + "MinMaxLengthError").removeClass("hide");
                    result = false;
                }
            } else {
                if (value.length < parseInt(minLength) || value.length > parseInt(maxLength)) {
                    this.replaceParameterInElementInnerHtml(classNamePrefix + "MinMaxLengthError", 1, minLength);
                    this.replaceParameterInElementInnerHtml(classNamePrefix + "MinMaxLengthError", 2, maxLength);
                    b2b.get(classNamePrefix + "MinMaxLengthError").removeClass("hide");
                    result = false;
                }
            }
            return result;
        },

        numberValidator: function (value, classNamePrefix, isRequired) {
            var result = true;
            var available = value !== undefined && value !== null && value !== "";
            if (!available) {
                if (isRequired) {
                    b2b.get(classNamePrefix + "NumberError").removeClass("hide");
                    result = false;
                }
            } else {
                if (isNaN(value)) {
                    b2b.get(classNamePrefix + "NumberError").removeClass("hide");
                    result = false;
                }
            }

            return result;
        },

        emailValidator: function (value, classNamePrefix) {
            var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            var result = re.test(value);
            if (!result) {
                b2b.get(classNamePrefix + "EmailError").removeClass("hide");
            }
            return result;
        },

        equalsValidator: function (value, classNamePrefix, isRequired, elementIdToCheckAgainst) {
            var result = true;
            var available = value !== undefined && value !== null && value !== "";
            if (!available) {
                if (isRequired) {
                    b2b.get(classNamePrefix + "EqualsError").removeClass("hide");
                    result = false;
                }
            } else {
                var valueToCheck = b2b.get("#" + elementIdToCheckAgainst).value();
                if (valueToCheck != value) {
                    b2b.get(classNamePrefix + "EqualsError").removeClass("hide");
                    result = false;
                }
            }
            return result;
        },

        replaceParameterInElementInnerHtml: function (elementelector, parameterNumber, parameterValue) {
            b2b.get(elementelector);
            b2b.replaceParameterInInnerHtml(parameterNumber, parameterValue);
        },

        clearValidationClasses: function (elementId) {
            if (b2b.get("#" + elementId).exists()) {
                var id = "#" + elementId;
                b2b.get(id).removeClass("form-control-success");
                b2b.get(id).removeClass("form-control-danger");
                b2b.get(id).removeClass("form-control-warning");

                b2b.get(id + "FormGroupWrapper").removeClass("has-success");
                b2b.get(id + "FormGroupWrapper").removeClass("has-danger");
                b2b.get(id + "FormGroupWrapper").removeClass("has-warning");

                var className = "." + elementId;
                b2b.get(className + "RequiredError").addClass("hide");
                b2b.get(className + "EqualsError").addClass("hide");
                b2b.get(className + "EmailError").addClass("hide");
                b2b.get(className + "NumberError").addClass("hide");
                b2b.get(className + "MinLengthError").addClass("hide");
                b2b.get(className + "MaxLengthError").addClass("hide");
                b2b.get(className + "MinMaxLengthError").addClass("hide");
                b2b.get(className + "Success").addClass("hide");
            }
        },

        showErrorValidationClasses: function (elementId) {
            var id = "#" + elementId;
            var className = "." + elementId;

            b2b.get(id + "FormGroupWrapper").addClass("has-danger");
            b2b.get(id).addClass("form-control-danger");
            b2b.get(className + "Error").removeClass("hide");
        },

        showSuccessValidationClasses: function (elementId) {
            var id = "#" + elementId;
            var className = "." + elementId;

            b2b.get(id + "FormGroupWrapper").addClass("has-success");
            b2b.get(id).addClass("form-control-success");
            b2b.get(className + "Success").removeClass("hide");
        }
    };
})();

var data = (function () {

    return {
        callback: undefined,

        /**
         * Uses a data url to retrieve the data and set the data on he loop elements, if any are found
         *
         * @param elementId the parent element id
         * @param dataUrl the url to retrieve the data with
         * @param callback a callback url if any is needed after creating the view
         */
        createElementForData: function (elementId, dataUrl, callback) {
            this.callback = callback;
            console.log("Parsing element : " + elementId);
            console.log("Getting data : http://localhost:8080/data/api/data/" + dataUrl);
            var parentElement = document.getElementById(elementId);
            var loopElement = this.findLoopElement(parentElement);
            loopElement.classList.add("hide");

            if (loopElement != undefined) {
                // loopElement removed and parent is known
                var loopPropertyVariableName = this.findAttributeOnElement(loopElement, "b2b-var");
                var loopPropertyName = this.findAttributeOnElement(loopElement, "b2b-loop");

                this.setData("http://localhost:8080/data/api/data/" + dataUrl, loopElement.parentNode, loopElement, loopPropertyName, loopPropertyVariableName);
            } else {
                alert("No loop element found");
            }
        },

        /**
         * Uses data to set the data on he loop elements, if any are found
         *
         * @param elementId the parent element id
         * @param data data sed, similar to @see{createElementForData} but without fetching
         */
        createElementForLocaleStorageData: function (elementId, data) {
            console.log("Parsing element : " + elementId);
            console.log("Using data : ", data);
            var parentElement = document.getElementById(elementId);
            var loopElement = this.findLoopElement(parentElement);
            loopElement.classList.add("hide");

            if (loopElement != undefined) {
                // loopElement removed and parent is known
                var loopPropertyVariableName = this.findAttributeOnElement(loopElement, "b2b-var");
                var loopPropertyName = this.findAttributeOnElement(loopElement, "b2b-loop");

                this.setDataOnLoopElementAndAddToDom(parentElement, loopElement, loopPropertyVariableName, data[loopPropertyName]);
            } else {
                alert("No loop element found");
            }
        },

        /**
         * Replaces b2b-data element within the element
         *
         * @param {*} parentElement the element on wich we will find a child with the b2b-data attribute on
         * @param {*} data the data to set on the b2b-data element
         */
        replaceDataOnElement(parentElement, data)
    {
        console.log("Setting data on element", parentElement, data);

        // Delete previously created template duplications, the template is hidden at this time
        b2b.get('#template_impl_id', parentElement).delete();

        var dataElements = b2b.get('[b2b-data]', parentElement);
        dataElements.removeClass("hide");
        console.log("Data elements found", dataElements);

        for (var i = 0; i < dataElements.selectedObjects.length; i++) {
            var element = dataElements.selectedObjects[i];
            var variableName = this.findAttributeOnElement(element, "b2b-data");
            console.log("Variable name", variableName);
            var clonedElement = element.cloneNode(true);
            console.log("Insert cloned element", clonedElement);
            parentElement.appendChild(clonedElement);
            console.log("Insert cloned element", clonedElement);
            var clonedElement = this.setDataOnDataSingleElement(clonedElement, clonedElement, variableName, data);
        }

        // hide the original elements, try to hide them first later !!
        dataElements.addClass("hide");
    }
    ,

    // USED FOR A SINGLE ELEMENT, REPLACE ALL PLACE HOLDERS b2b{{%%KEY%%}}
    /**
     * Replaces b2b{{ %%PROPERTY_NAME%% }} placeholders
     *
     * @param parentElement the element that contains the b2b-data attribute
     * @param element the first time this will be the same object, secondary calls for children are with child elements
     */
    setDataOnDataSingleElement: function (parentElement, element, loopPropertyVariableName, data) {
        var record = data;
        this.setDataOnElement(element, loopPropertyVariableName, record);
        parentElement.id = "template_impl_id";
        return parentElement;
    }
    ,

    // USED FOR A LOOP ELEMENT, REPLACE ALL PLACE HOLDERS b2b{{%%KEY%%}}
    setDataOnLoopElementAndAddToDom: function (parentElement, loopElement, loopPropertyVariableName, data) {
        loopElement.classList.add("hide");
        for (var k = 0; k < data.length; k++) {
            var record = data[k];
            var element = loopElement.cloneNode(true);
            parentElement.appendChild(element);

            this.handleValueElements(element);
            this.handleTypedElements(element, loopPropertyVariableName, record);

            this.setDataOnElement(element, loopPropertyVariableName, record);
            element.classList.remove("hide");

            // Iterate children
            var children = element.children;
            while (children != undefined && children != null && children.length > 0) {
                var newChildren = [];
                for (var i = 0; i < children.length; i++) {
                    var child = children[i];

                    // Find child loop in child element
                    var childLoopElement = this.findLoopElement(child);
                    if (childLoopElement != undefined) {
                        var childLoopPropertyName = this.findAttributeOnElement(childLoopElement, "b2b-loop");
                        var childLoopPropertyVariableName = this.findAttributeOnElement(childLoopElement, "b2b-var");

                        if (childLoopPropertyName != undefined) {
                            var propertyName = childLoopPropertyName.replace(loopPropertyVariableName + ".", "");
                            if (record[propertyName] != undefined) {
                                var newData = record[propertyName];
                                this.setDataOnLoopElementAndAddToDom(childLoopElement.parentNode, childLoopElement, childLoopPropertyVariableName, newData);
                            } else {
                                this.setDataOnLoopElementAndAddToDom(childLoopElement.parentNode, childLoopElement, childLoopPropertyVariableName, data);
                            }
                        }
                    }

                    if (child.children != undefined && child.children != null && child.children.length > 0) {
                        for (var j = 0; j < child.children.length; j++) {
                            newChildren.push(child.children[j]);
                        }
                    }
                }

                children = newChildren;
            }
        }

        return element;
    }
    ,

    handleValueElements: function (element) {
        var valueElements = this.findValueElements(element);
        for (var l = 0; l < valueElements.length; l++) {
            var value = this.findAttributeOnElement(valueElements[l], "b2b-value");

            var properties = value.split(" ");
            value = "";
            if (properties.length == 1) {
                value += "b2b{{" + properties[0] + "}}";
            } else {
                for (var i = 0; i < properties.length; i++) {
                    value += "b2b{{" + properties[i] + "}} ";
                }
            }

            console.log("Value Element", valueElements[l]);
            valueElements[l].setAttribute("value", value);
            valueElements[l].innerHTML = value;
        }
    }
    ,

    handleTypedElements: function (element, loopPropertyVariableName, record) {
        // handle typed elements
        var typedElements = this.findTypedElements(element);
        for (l = 0; l < typedElements.length; l++) {
            var type = this.findAttributeOnElement(typedElements[l], "b2b-type");
            this.setDataOnElement(typedElements[l], loopPropertyVariableName, record, type);
        }
    }
    ,

    setDataOnElement: function (loopElement, loopPropertyVariableName, data, type) {
        if (!this.isObjectRemovedDueToIfStatement(loopElement, loopPropertyVariableName, data)) {
            // replace placeholders
            for (var key in data) {
                if (type == "price") {
                    if (!isNaN(data[key]) && !(typeof data[key] == "string")) {
                        this.replaceOnElement(loopElement, "b2b{{" + loopPropertyVariableName + "." + key + "}}", data[key].toFixed(2));
                    }
                } else {
                    this.replaceOnElement(loopElement, "b2b{{" + loopPropertyVariableName + "." + key + "}}", data[key]);
                }
            }
        }

        return loopElement;
    }
    ,

    isObjectRemovedDueToIfStatement: function (element, loopPropertyVariableName, data) {
        var ifStatement = this.findAttributeOnElement(element, "b2b-if");

        if (ifStatement != undefined) {
            // console.log("If statement", element, ifStatement, loopPropertyVariableName, data);
            ifStatement = ifStatement.replace(loopPropertyVariableName, "data");
            // console.log("Final If statement", data.code, ifStatement, eval(ifStatement));

            if (!eval(ifStatement)) {
                element.parentNode.removeChild(element);
                // console.log("Element removed due to if statement", element);
            }
        }

        return ifStatement != undefined;
    }
    ,

    setData: function (url, parentElement, loopElement, loopPropertyName, loopPropertyVariableName) {
        var xhr = new XMLHttpRequest();
        var context = this;
        xhr.open('GET', url);

        xhr.onload = function () {
            if (xhr.status === 200) {
                var data = JSON.parse(JSON.parse(xhr.response).data);
                console.log("Data successfully retrieved", data);
                console.log("Loop element found", loopElement);
                console.log("Loop element property found", loopPropertyName);
                console.log("Loop element variable found", loopPropertyVariableName);
                context.setDataOnLoopElementAndAddToDom(parentElement, loopElement, loopPropertyVariableName, data[loopPropertyName]);

                context.callback.call();
            } else {
                //context.errorHandler.call(this, xhr);
            }
        };

        xhr.send();
    }
    ,

    findLoopElement: function (element) {
        var items = element.getElementsByTagName("*");

        for (var i = 0; i < items.length; i++) {
            var currentTag = items[i];

            loopPropertyName = this.findAttributeOnElement(currentTag, "b2b-loop");

            if (loopPropertyName != null) {
                return currentTag;
            }
        }

        return undefined;
    }
    ,

    findTypedElements: function (element) {
        var items = element.getElementsByTagName("*");

        var result = [];

        for (var i = 0; i < items.length; i++) {
            var currentTag = items[i];

            loopPropertyName = this.findAttributeOnElement(currentTag, "b2b-type");

            if (loopPropertyName != null) {
                result.push(currentTag);
            }
        }

        return result;
    }
    ,

    findValueElements: function (element) {
        var items = element.getElementsByTagName("*");

        var result = [];

        for (var i = 0; i < items.length; i++) {
            var currentTag = items[i];

            loopPropertyName = this.findAttributeOnElement(currentTag, "b2b-value");

            if (loopPropertyName != null) {
                result.push(currentTag);
            }
        }

        return result;
    }
    ,

    findAttributeOnElement: function (element, attributeName) {
        if (element.attributes != undefined) {
            for (var i = 0; i < element.attributes.length; i++) {
                var attribute = element.attributes[i];
                if (attribute.specified && attribute.name == attributeName) {
                    return attribute.value;
                }
            }
        }

        return undefined;
    }
    ,

    replaceOnElement: function (element, replaceProperty, replaceValue) {
        //console.log("Replace on element", element, replaceProperty, replaceValue);

        if (element.innerHTML.indexOf(replaceProperty) != -1) {
            element.innerHTML = element.innerHTML.replace(new RegExp(replaceProperty, 'g'), replaceValue);
        }
        if (element.attributes != undefined) {
            for (var i = 0; i < element.attributes.length; i++) {
                var attribute = element.attributes[i];
                if (attribute.value.indexOf(replaceProperty) != -1) {
                    if (attribute.specified) {
                        element.setAttribute(attribute.name, attribute.value.replace(replaceProperty, replaceValue));
                    }
                }
            }
        }
    }

};
})
();

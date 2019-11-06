import { NgModule, ModuleWithProviders }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgxUploaderModule } from 'ngx-uploader';

import { TranslateModule } from '@ngx-translate/core';

import {
  BaThemeConfig
} from './theme.config';

import {
  BaThemeConfigProvider
} from './theme.configProvider';

import {
    BaBackTop,
    BaCard,
    BaCheckbox,
    BaContentTop,
    BaMenuItem,
    BaMenu,
    BaMsgCenter,
    BaMultiCheckbox,
    BaPageTop,
    BaSidebar,
    MonacoEditorComponent,
    JsonSchemaFormComponent
} from './components';

import { BaCardBlur } from './components/baCard/baCardBlur.directive';

import {
    BaScrollPosition,
    BaThemeRun,
    B2BCurrencyFormatterDirective,
    BTCCurrencyFormatterDirective,
    B2BCurrencyPipe,
    BTCCurrencyPipe
} from './directives';

import {
    BaAppPicturePipe,
    BaKameleonPicturePipe,
    BaProfilePicturePipe
} from './pipes';

import {
    BaImageLoaderService,
    BaMenuService,
    BaThemePreloader,
    BaThemeSpinner
} from './services';

import {
    EmailValidator,
    EqualPasswordsValidator
} from './validators';

const NGA_COMPONENTS = [
    BaBackTop,
    BaCard,
    BaCheckbox,
    BaContentTop,
    BaMenuItem,
    BaMenu,
    BaMsgCenter,
    BaMultiCheckbox,
    BaPageTop,
    BaSidebar,
    MonacoEditorComponent,
    JsonSchemaFormComponent
];

const NGA_DIRECTIVES = [
    BaScrollPosition,
    BaThemeRun,
    BaCardBlur,
    B2BCurrencyFormatterDirective,
    BTCCurrencyFormatterDirective
];

const NGA_PIPES = [
    BaAppPicturePipe,
    BaKameleonPicturePipe,
    BaProfilePicturePipe,
    B2BCurrencyPipe,
    BTCCurrencyPipe
];

const NGA_SERVICES = [
    BaImageLoaderService,
    BaThemePreloader,
    BaThemeSpinner,
    BaMenuService
];

const NGA_VALIDATORS = [
    EmailValidator,
    EqualPasswordsValidator
];

@NgModule({
    declarations: [
        ...NGA_PIPES,
        ...NGA_DIRECTIVES,
        ...NGA_COMPONENTS
    ],
    imports: [
        CommonModule,
        RouterModule,
        FormsModule,
        ReactiveFormsModule,
        NgxUploaderModule,
        TranslateModule.forRoot()
    ],
    exports: [
        ...NGA_PIPES,
        ...NGA_DIRECTIVES,
        ...NGA_COMPONENTS
    ]
})
export class NgaModule {
    static forRoot (): ModuleWithProviders {
        return <ModuleWithProviders> {
            ngModule: NgaModule,
            providers: [
                BaThemeConfigProvider,
                BaThemeConfig,
                B2BCurrencyPipe,
                BTCCurrencyPipe,
                ...NGA_VALIDATORS,
                ...NGA_SERVICES
            ],
        };
    }
}

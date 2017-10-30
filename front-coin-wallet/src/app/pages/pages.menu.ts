export const PAGES_MENU = [
    {
        path: 'pages',
        children: [
            {
                path: 'dashboard',
                data: {
                    menu: {
                        title: 'Dashboard',
                        icon: 'ion-android-home',
                        selected: false,
                        expanded: false,
                        order: 0
                    }
                }
            },
            {
                path: 'addresses',
                data: {
                    menu: {
                        title: 'Addresses',
                        icon: 'ion-ios-book-outline',
                        selected: false,
                        expanded: false,
                        order: 250,
                    }
                },
                children: [
                    {
                        path: 'overview',
                        data: {
                            menu: {
                                title: 'Overview',
                            }
                        }
                    }
                ]
            },
            {
                path: 'transactions',
                data: {
                    menu: {
                        title: 'Blockchain',
                        icon: 'ion-link',
                        selected: false,
                        expanded: false,
                        order: 250,
                    }
                },
                children: [
                    {
                        path: 'result',
                        data: {
                            menu: {
                                title: 'Explorer',
                            }
                        }
                    }
                ]
            }
            ,
            {
                path: 'payments',
                data: {
                    menu: {
                        title: 'Payments',
                        icon: 'ion-cash',
                        selected: false,
                        expanded: false,
                        order: 250,
                    }
                },
                children: [
                    {
                        path: 'overview',
                        data: {
                            menu: {
                                title: 'Overview',
                            }
                        }
                    },
                    {
                        path: 'create',
                        data: {
                            menu: {
                                title: 'Create payment',
                            }
                        }
                    }
                ]
            }
            ,
            {
                path: 'transactions',
                data: {
                    menu: {
                        title: 'Transactions',
                        icon: 'ion-network',
                        selected: false,
                        expanded: false,
                        order: 250,
                    }
                },
                children: [
                    {
                        path: 'overview',
                        data: {
                            menu: {
                                title: 'Overview',
                            }
                        }
                    }
                ]
            }
        ]
    }
];

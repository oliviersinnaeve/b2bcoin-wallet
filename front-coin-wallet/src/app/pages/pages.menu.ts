export const PAGES_MENU = [
    {
        path: 'pages',
        children: [
            {
                path: 'dashboard/mainWallet/',
                data: {
                    menu: {
                        title: "PAGES_MENU.DASHBOARD",
                        icon: 'ion-android-home',
                        selected: false,
                        expanded: false,
                        order: 0
                    }
                }
            },
            {
                path: 'coins',
                data: {
                    menu: {
                        title: "PAGES_MENU.COIN_OVERVIEW",
                        icon: 'ion-ios-book-outline',
                        selected: false,
                        expanded: false,
                        order: 250,
                    }
                }
            },
            {
                path: 'explorer',
                data: {
                    menu: {
                        title: "PAGES_MENU.BLOCKCHAIN",
                        icon: 'ion-link',
                        selected: false,
                        expanded: false,
                        order: 250,
                    }
                }
            },
            {
                path: 'faucet',
                data: {
                    menu: {
                        title: "PAGES_MENU.FAUCET",
                        icon: 'ion-funnel',
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
                                title: "PAGES_MENU.OVERVIEW",
                            }
                        }
                    }
                    ,
                    {
                        path: 'user',
                        data: {
                            menu: {
                                title: "PAGES_MENU.FAUCET_USER",
                            }
                        }
                    }
                    ,
                    {
                        path: 'owner',
                        data: {
                            menu: {
                                title: "PAGES_MENU.FAUCET_OWNER",
                            }
                        }
                    }
                ]
            }
        ]
    }
];

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
            //    children: [
            //        {
            //            path: 'result',
            //            data: {
            //                menu: {
            //                    title: 'Explorer',
            //                }
            //            }
            //        }
            //    ]
            //}
            //,
            //{
            //    path: 'payments',
            //    data: {
            //        menu: {
            //            title: 'Payments',
            //            icon: 'ion-cash',
            //            selected: false,
            //            expanded: false,
            //            order: 250,
            //        }
            //    },
            //    children: [
            //        {
            //            path: 'overview',
            //            data: {
            //                menu: {
            //                    title: 'Overview',
            //                }
            //            }
            //        },
            //        {
            //            path: 'create',
            //            data: {
            //                menu: {
            //                    title: 'Create payment',
            //                }
            //            }
            //        }
            //    ]
            //}
            //,
            //{
            //    path: 'transactions',
            //    data: {
            //        menu: {
            //            title: 'Transactions',
            //            icon: 'ion-network',
            //            selected: false,
            //            expanded: false,
            //            order: 250,
            //        }
            //    },
            //    children: [
            //        {
            //            path: 'overview',
            //            data: {
            //                menu: {
            //                    title: 'Overview',
            //                }
            //            }
            //        }
            //    ]
            //}
            //,
            {
                path: 'escrow',
                data: {
                    menu: {
                        title: "PAGES_MENU.ESCROW",
                        icon: 'ion-shuffle',
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
                        path: 'create',
                        data: {
                            menu: {
                                title: "PAGES_MENU.CREATE_TRANSACTION",
                            }
                        }
                    }
                ]
            }
        ]
    }
];

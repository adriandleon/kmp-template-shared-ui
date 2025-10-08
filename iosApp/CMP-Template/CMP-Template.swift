//
//  CMP-Template.swift
//  CMP-Template
//
//  Created by Adrian De León on 5/9/25.
//  Copyright © 2025 CMP-Template. All rights reserved.
//

import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
                }
        }
    }
}


#import "RNDeltaDNA.h"
#import "RCTConvert.h"
#import <DeltaDNA/DeltaDNA.h>

@implementation RNDeltaDNA

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

- (void) startWithEnvironmentKey:(NSString *) environmentKey collectURL:(NSString *) collectURL engageURL:(NSString *) engageURL
{
    [[DDNASDK sharedInstance] startWithEnvironmentKey:environmentKey collectURL:collectURL engageURL:engageURL];
}

- (void) startWithEnvironmentKey:(NSString *) environmentKey collectURL:(NSString *) collectURL engageURL:(NSString *) engageURL userID:(NSString *) userID
{
    [[DDNASDK sharedInstance] startWithEnvironmentKey:environmentKey collectURL:collectURL engageURL:engageURL userID:userID];
}

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(start: (NSDictionary *) dict)
{
    if (dict[@"userID"] == nil) {
        [self startWithEnvironmentKey:dict[@"environmentKey"] collectURL:dict[@"collectURL"] engageURL:dict[@"engageURL"]];
    } else {
        [self startWithEnvironmentKey:dict[@"environmentKey"] collectURL:dict[@"collectURL"] engageURL:dict[@"engageURL"] userID:dict[@"userID"]];
    }
}

RCT_EXPORT_METHOD(stop)
{
    [[DDNASDK sharedInstance] stop];
}

RCT_EXPORT_METHOD(upload)
{
    [[DDNASDK sharedInstance] upload];
}

RCT_EXPORT_METHOD(clearPersistentData)
{
    [[DDNASDK sharedInstance] clearPersistentData];
}

RCT_EXPORT_METHOD(newSession)
{
    [[DDNASDK sharedInstance] newSession];
}

RCT_EXPORT_METHOD(setClientVersion: (NSString *) version)
{
    [[DDNASDK sharedInstance] setClientVersion:version];
}

RCT_EXPORT_METHOD(setSessionTimeoutSeconds: (int) seconds)
{
    [[DDNASDK sharedInstance].settings setSessionTimeoutSeconds:seconds];
}

RCT_EXPORT_METHOD(setHashSecret: (NSString *) secret)
{
    [[DDNASDK sharedInstance] setHashSecret:secret];
}

RCT_EXPORT_METHOD(recordEvent: (NSDictionary *) eventDict)
{
    DDNAEvent *event = [DDNAEvent eventWithName:eventDict[@"name"]];

    if (eventDict[@"params"] != nil) {
        for (NSString *key in [eventDict[@"params"] allKeys]) {
            [event setParam:eventDict[@"params"][key] forKey:key];
        }
    }

    if (eventDict[@"values"] != nil) {
        for (NSString *key in [eventDict[@"values"] allKeys]) {
            [event setValue:eventDict[@"values"][key] forKey:key];
        }
    }

    [[DDNASDK sharedInstance] recordEvent:event];
}

RCT_EXPORT_METHOD(recordPushNotification: (NSDictionary *) payload didLaunch: (BOOL) didLaunch)
{
    [[DDNASDK sharedInstance] recordPushNotification:payload didLaunch:didLaunch];
}

RCT_EXPORT_METHOD(engage: (NSDictionary *) engageDict callback:(RCTResponseSenderBlock)callback)
{
    DDNAEngagement *engagement = [DDNAEngagement engagementWithDecisionPoint:engageDict[@"name"]];

    if (engageDict[@"params"] != nil) {
        for (NSString *key in [engageDict[@"params"] allKeys]) {
            [engagement setParam:engageDict[@"params"][key] forKey:key];
        }
    }

    if (engageDict[@"values"] != nil) {
        for (NSString *key in [engageDict[@"values"] allKeys]) {
            [engagement setValue:engageDict[@"values"][key] forKey:key];
        }
    }

    [[DDNASDK sharedInstance] requestEngagement:engagement engagementHandler:^(DDNAEngagement *response) {
        callback(response.json);
    }];
}

@end

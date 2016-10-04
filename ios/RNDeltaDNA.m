
#import "RNDeltaDNA.h"
#import "RCTConvert.h"
#import <DeltaDNA/DeltaDNA.h>

@implementation RNDeltaDNA

- (dispatch_queue_t) methodQueue
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
    if (dict[@"settings"]) {
        [[[DDNASDK sharedInstance] settings] setValuesForKeysWithDictionary:dict[@"options"]];
    }
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

    NSDictionary *paramsDict = eventDict[@"params"];

    if (paramsDict != nil) {
        for (NSString *key in paramsDict) {
            id value = [paramsDict objectForKey:key];
            [event setParam:value forKey:key];
        }
    }

    [[DDNASDK sharedInstance] recordEvent:event];
}

RCT_EXPORT_METHOD(recordPushNotification: (NSDictionary *) payload didLaunch: (BOOL) didLaunch)
{
    if (payload != nil) {
        [[DDNASDK sharedInstance] recordPushNotification:payload didLaunch:didLaunch];
    }
}

RCT_EXPORT_METHOD(engage: (NSDictionary *) engageDict callback:(RCTResponseSenderBlock)callback)
{
    DDNAEngagement *engagement = [DDNAEngagement engagementWithDecisionPoint:engageDict[@"name"]];

    NSDictionary *paramsDict = engageDict[@"params"];

    if (paramsDict != nil) {
        for (NSString *key in paramsDict) {
            id value = [paramsDict objectForKey:key];
            [engagement setParam:value forKey:key];
        }
    }

    [[DDNASDK sharedInstance] requestEngagement:engagement engagementHandler:^(DDNAEngagement *response) {
        callback(response.json);
    }];
}

@end


#import "RNDeltaDNA.h"
#import "RCTConvert.h"

@implementation RNDeltaDNA

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(test)
{
  // Your implementation here
}

@end

@import CobrowseIO;
#import "CBIORCTUtil.h"


@implementation CBIOSession (Bridging)

-(NSDictionary*) toDict {
    return @{
        @"id": self.id ? self.id : NSNull.null,
        @"code": self.code ? self.code : NSNull.null,
        @"state": self.state ? self.state : NSNull.null,
        @"full_device": @(self.fullDevice),
        @"full_device_state": [CBIORCTUtil fullDeviceState: self.fullDeviceState],
        @"remote_control": [CBIORCTUtil remoteControl: self.remoteControl],
        @"agent": self.hasAgent ? @{
            @"name": self.agent.name,
            @"id": self.agent.id
        } : NSNull.null
    };
}

@end

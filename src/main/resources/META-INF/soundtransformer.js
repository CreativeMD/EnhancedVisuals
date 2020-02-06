function initializeCoreMod() {
	print("Init CreativeCore coremods ...")
    return {
        'clientnetwork': {
            'target': {
                'type': 'METHOD',
				'class': 'net.minecraft.client.audio.SoundEngine',
				'methodName': 'func_148611_c',
				'methodDesc': '(Lnet/minecraft/client/audio/ISound;)V'
            },
            'transformer': function(method) {
				var asmapi = Java.type('net.minecraftforge.coremod.api.ASMAPI');

				var node = asmapi.findFirstMethodCall(method, asmapi.MethodType.SPECIAL, "net/minecraft/client/audio/SoundEngine", "getClampedVolume", "(Lnet/minecraft/client/audio/ISound;)F");
				method.instructions.remove(node.getPrevious().getPrevious());
				method.instructions.set(node, asmapi.buildMethodCall("team/creative/enhancedvisuals/client/sound/SoundMuteHandler", "getClampedVolume", "(Lnet/minecraft/client/audio/ISound;)F", asmapi.MethodType.STATIC));

                return method;
            }
		}
    }
}

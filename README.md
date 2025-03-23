Preparation for IDEA:

Clone Repo

Do your workspace setup if needed, but IDEA should do it all for you

Go to Settings > Plugins > (Gear) > Manage Plugin Repositories...

Add `https://raw.githubusercontent.com/eigenraven/MinecraftDev/rfg/updates/updatePlugins-243.xml` (for 2024.3. other versions change the last digit)

Close and install the Minecraft Development plugin at the bottom of the list

Place unimixins and `ExtraBotany-1.7.10-r1.0-21` jar files in `/modlibs`

Environment should be ready.

___

Eclipse上ではどうしても実行することができなかったので、ビルドする際は、同梱の"build.bat"を実行してください。
"build/libs/"に出力されたjarを実機にて、確認してください。

___

Since it was not possible to run the program on Eclipse, please run the included “build.bat” to build the program.
Check the jar output to “build/libs/” on the actual device.

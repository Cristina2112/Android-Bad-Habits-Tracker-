$old = "JichiCristina_MoldovanPaul"
$new = "jichicristina_moldovanpaul"
Get-ChildItem -Path app -Recurse -File | Where-Object { $_.Extension -match '\.(kt|xml|kts)$' } | ForEach-Object {
    $c = Get-Content $_.FullName
    if ($c -match $old) {
        $c = $c -replace $old, $new
        Set-Content -Path $_.FullName -Value $c
    }
}
Rename-Item -Path "app\src\main\java\com\JichiCristina_MoldovanPaul" -NewName $new -ErrorAction SilentlyContinue
Rename-Item -Path "app\src\androidTest\java\com\JichiCristina_MoldovanPaul" -NewName $new -ErrorAction SilentlyContinue
Rename-Item -Path "app\src\test\java\com\JichiCristina_MoldovanPaul" -NewName $new -ErrorAction SilentlyContinue

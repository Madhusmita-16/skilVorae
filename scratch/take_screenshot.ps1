param(
    [string]$Url,
    [string]$OutFile
)

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

# Find Chrome or Edge
$browsers = @(
    "${env:ProgramFiles}\Google\Chrome\Application\chrome.exe",
    "${env:ProgramFiles(x86)}\Google\Chrome\Application\chrome.exe",
    "${env:LOCALAPPDATA}\Google\Chrome\Application\chrome.exe",
    "${env:ProgramFiles}\Microsoft\Edge\Application\msedge.exe",
    "${env:ProgramFiles(x86)}\Microsoft\Edge\Application\msedge.exe"
)

$browser = $null
foreach ($b in $browsers) {
    if (Test-Path $b) { $browser = $b; break }
}

if (-not $browser) {
    Write-Error "Chrome or Edge not found"
    exit 1
}

Write-Host "Using browser: $browser"
Write-Host "Opening: $Url"

# Open browser with the URL in headless screenshot mode
$args = "--headless --disable-gpu --screenshot=`"$OutFile`" --window-size=1280,800 `"$Url`""
Start-Process -FilePath $browser -ArgumentList $args -Wait

Write-Host "Screenshot saved to: $OutFile"

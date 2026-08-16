<#
.SYNOPSIS
Maven Wrapper PowerShell script
#>

[CmdletBinding()]
param(
    [Parameter(Position = 0, ValueFromRemainingArguments = $true)]
    [string[]]$args
)

$ErrorActionPreference = 'Stop'

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectDir = Split-Path -Parent (Split-Path -Parent $scriptDir)
$propFile = Join-Path $scriptDir 'maven-wrapper.properties'

if (!(Test-Path $propFile)) {
    Write-Error "Cannot find $propFile"
    exit 1
}

$props = Get-Content $propFile | ConvertFrom-StringData
$distUrl = $props.distributionUrl

if (!$distUrl) {
    Write-Error "distributionUrl property not found in $propFile"
    exit 1
}

$mvnVersion = ($distUrl -split '/')[-2]
if (!$mvnVersion) {
    $mvnVersion = "3.9.9"
}

$userHome = [System.Environment]::GetFolderPath('UserProfile')
$mvnHome = Join-Path $userHome ".m2\wrapper\dists\apache-maven-$mvnVersion"

$mvnBin = Join-Path $mvnHome "apache-maven-$mvnVersion\bin\mvn.cmd"

if (!(Test-Path $mvnBin)) {
    Write-Host "Downloading Maven $mvnVersion from $distUrl ..."
    New-Item -ItemType Directory -Force -Path $mvnHome | Out-Null
    $zipPath = Join-Path $mvnHome "apache-maven-$mvnVersion-bin.zip"
    
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $distUrl -OutFile $zipPath
    
    Write-Host "Extracting Maven distribution to $mvnHome ..."
    Expand-Archive -Path $zipPath -DestinationPath $mvnHome -Force
    Remove-Item $zipPath -Force
}

$jdk21 = "C:\Program Files\Java\jdk-21.0.12"
if (Test-Path $jdk21) {
    $env:JAVA_HOME = $jdk21
}

$javaBin = "java"
if ($env:JAVA_HOME) {
    $javaBin = Join-Path $env:JAVA_HOME "bin\java.exe"
}

& $mvnBin @args
exit $LASTEXITCODE

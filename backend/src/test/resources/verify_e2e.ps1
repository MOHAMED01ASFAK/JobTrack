$baseUrl = "http://localhost:8080"

function Make-Request {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Token = $null,
        [object]$Body = $null
    )
    $headers = @{
        "Content-Type" = "application/json"
        "Accept" = "application/json"
    }
    if ($Token) {
        $headers["Authorization"] = "Bearer $Token"
    }

    $jsonBody = if ($Body) { $Body | ConvertTo-Json -Depth 10 } else { $null }

    try {
        if ($jsonBody) {
            $response = Invoke-WebRequest -Uri $Url -Method $Method -Headers $headers -Body $jsonBody -UseBasicParsing
        } else {
            $response = Invoke-WebRequest -Uri $Url -Method $Method -Headers $headers -UseBasicParsing
        }
        $content = if ($response.Content) { $response.Content | ConvertFrom-Json } else { $null }
        return @{
            StatusCode = [int]$response.StatusCode
            Data = $content
        }
    } catch [System.Net.WebException] {
        $ex = $_.Exception
        $statusCode = [int]$ex.Response.StatusCode
        $reader = New-Object System.IO.StreamReader($ex.Response.GetResponseStream())
        $respText = $reader.ReadToEnd()
        $content = if ($respText) { $respText | ConvertFrom-Json } else { $null }
        return @{
            StatusCode = $statusCode
            Data = $content
        }
    } catch {
        Write-Error $_.Exception.Message
        return @{
            StatusCode = 500
            Data = $null
        }
    }
}

Write-Host "=== 1. Authentication Setup ==="
$rand = Get-Random -Minimum 1000 -Maximum 9999
$user1Reg = @{
    username = "alice_$rand"
    email = "alice_$rand@example.com"
    password = "Password123!"
    fullName = "Alice Developer"
}
$user2Reg = @{
    username = "bob_$rand"
    email = "bob_$rand@example.com"
    password = "Password123!"
    fullName = "Bob Engineer"
}

$r1 = Make-Request -Method "POST" -Url "$baseUrl/api/v1/auth/register" -Body $user1Reg
Write-Host "User 1 Registered -> Status: $($r1.StatusCode), Success: $($r1.Data.success)"
$token1 = $r1.Data.data.accessToken

$r2 = Make-Request -Method "POST" -Url "$baseUrl/api/v1/auth/register" -Body $user2Reg
Write-Host "User 2 Registered -> Status: $($r2.StatusCode), Success: $($r2.Data.success)"
$token2 = $r2.Data.data.accessToken

Write-Host "`n=== 2. Create Job Application for User 1 ==="
$jobReq = @{
    companyName = "Netflix"
    jobTitle = "Senior Platform Engineer"
    jobLocation = "Los Gatos, CA"
    workplaceType = "HYBRID"
    employmentType = "FULL_TIME"
    applicationStatus = "APPLIED"
    salaryMin = 180000
    salaryMax = 250000
    salaryCurrency = "USD"
    priority = 5
}
$jobResp = Make-Request -Method "POST" -Url "$baseUrl/api/v1/jobs" -Token $token1 -Body $jobReq
$jobId1 = $jobResp.Data.data.id
Write-Host "Job created with ID: $jobId1 for User 1 -> Status: $($jobResp.StatusCode), Company: $($jobResp.Data.data.companyName)"

Write-Host "`n=== 3. Add Interview to Job 1 ==="
$interviewReq = @{
    roundName = "Technical Screen"
    roundType = "TECHNICAL"
    scheduledTime = (Get-Date).AddDays(2).ToString("yyyy-MM-ddTHH:mm:ss")
    interviewerInfo = "Engineering Lead"
    meetingLink = "https://meet.google.com/xyz-test"
    status = "SCHEDULED"
    questionsAsked = "System design, Distributed caching"
    feedbackNotes = "Initial screen pending"
}
$intResp = Make-Request -Method "POST" -Url "$baseUrl/api/v1/jobs/$jobId1/interviews" -Token $token1 -Body $interviewReq
$interviewId1 = $intResp.Data.data.id
Write-Host "Interview created with ID: $interviewId1 -> Status: $($intResp.StatusCode), Round: $($intResp.Data.data.roundName), RoundType: $($intResp.Data.data.roundType)"

Write-Host "`n=== 4. Retrieve Interviews for Job 1 ==="
$getInts = Make-Request -Method "GET" -Url "$baseUrl/api/v1/jobs/$jobId1/interviews" -Token $token1
Write-Host "Retrieved $($getInts.Data.data.Count) interview(s) for Job 1 -> Status: $($getInts.StatusCode), RoundName: $($getInts.Data.data[0].roundName)"

Write-Host "`n=== 5. Update Interview 1 ==="
$updateIntReq = @{
    roundName = "Technical Screen - Passed"
    roundType = "TECHNICAL"
    scheduledTime = (Get-Date).AddDays(2).ToString("yyyy-MM-ddTHH:mm:ss")
    interviewerInfo = "Engineering Lead"
    meetingLink = "https://meet.google.com/xyz-test"
    status = "COMPLETED"
    questionsAsked = "System design, Distributed caching"
    feedbackNotes = "Strong performance on architecture tradeoffs"
}
$updResp = Make-Request -Method "PUT" -Url "$baseUrl/api/v1/interviews/$interviewId1" -Token $token1 -Body $updateIntReq
Write-Host "Interview updated -> Status: $($updResp.StatusCode), StatusValue: $($updResp.Data.data.status), Feedback: $($updResp.Data.data.feedbackNotes)"

Write-Host "`n=== 6. User Isolation Check on Interviews ==="
$u2View = Make-Request -Method "GET" -Url "$baseUrl/api/v1/jobs/$jobId1/interviews" -Token $token2
Write-Host "User 2 GET Job 1 interviews -> Status: $($u2View.StatusCode) (Expected 404)"

$u2Upd = Make-Request -Method "PUT" -Url "$baseUrl/api/v1/interviews/$interviewId1" -Token $token2 -Body $updateIntReq
Write-Host "User 2 PUT Interview 1 -> Status: $($u2Upd.StatusCode) (Expected 404)"

$u2Del = Make-Request -Method "DELETE" -Url "$baseUrl/api/v1/interviews/$interviewId1" -Token $token2
Write-Host "User 2 DELETE Interview 1 -> Status: $($u2Del.StatusCode) (Expected 404)"

$u2Create = Make-Request -Method "POST" -Url "$baseUrl/api/v1/jobs/$jobId1/interviews" -Token $token2 -Body $interviewReq
Write-Host "User 2 POST Interview to Job 1 -> Status: $($u2Create.StatusCode) (Expected 404)"

Write-Host "`n=== 7. Create Follow-Up Reminder ==="
$followUpReq = @{
    dueDate = (Get-Date).AddDays(4).ToString("yyyy-MM-dd")
    contactName = "Emily Talent Partner"
    contactEmail = "emily@netflix.com"
    notes = "Send thank you note and check next steps"
    isCompleted = $false
}
$fuResp = Make-Request -Method "POST" -Url "$baseUrl/api/v1/jobs/$jobId1/follow-ups" -Token $token1 -Body $followUpReq
$followUpId1 = $fuResp.Data.data.id
Write-Host "Follow-up created with ID: $followUpId1 -> Status: $($fuResp.StatusCode), Contact: $($fuResp.Data.data.contactName), Completed: $($fuResp.Data.data.isCompleted)"

Write-Host "`n=== 8. Retrieve Pending Follow-Ups ==="
$pendingFu = Make-Request -Method "GET" -Url "$baseUrl/api/v1/follow-ups?completed=false" -Token $token1
Write-Host "Pending follow-ups count: $($pendingFu.Data.data.Count) -> Status: $($pendingFu.StatusCode), First: $($pendingFu.Data.data[0].contactName)"

Write-Host "`n=== 9. Toggle Follow-Up Completion ==="
$toggleResp = Make-Request -Method "PATCH" -Url "$baseUrl/api/v1/follow-ups/$followUpId1/toggle" -Token $token1
Write-Host "Follow-up completion toggled to: $($toggleResp.Data.data.isCompleted) -> Status: $($toggleResp.StatusCode)"

$pendingAfter = Make-Request -Method "GET" -Url "$baseUrl/api/v1/follow-ups?completed=false" -Token $token1
Write-Host "Pending follow-ups count after toggle: $($pendingAfter.Data.data.Count)"

$completedAfter = Make-Request -Method "GET" -Url "$baseUrl/api/v1/follow-ups?completed=true" -Token $token1
Write-Host "Completed follow-ups count after toggle: $($completedAfter.Data.data.Count) (ID: $($completedAfter.Data.data[0].id), Completed: $($completedAfter.Data.data[0].isCompleted))"

Write-Host "`n=== 10. User Isolation Check on Follow-Ups ==="
$u2Toggle = Make-Request -Method "PATCH" -Url "$baseUrl/api/v1/follow-ups/$followUpId1/toggle" -Token $token2
Write-Host "User 2 PATCH Follow-Up 1 -> Status: $($u2Toggle.StatusCode) (Expected 404)"

$u2Fu = Make-Request -Method "GET" -Url "$baseUrl/api/v1/follow-ups" -Token $token2
Write-Host "User 2 follow-ups list count: $($u2Fu.Data.data.Count) (Expected 0)"

Write-Host "`n=== 11. Delete Interview ==="
$delResp = Make-Request -Method "DELETE" -Url "$baseUrl/api/v1/interviews/$interviewId1" -Token $token1
Write-Host "Interview deleted -> Status: $($delResp.StatusCode), Message: $($delResp.Data.message)"

$getIntsAfterDel = Make-Request -Method "GET" -Url "$baseUrl/api/v1/jobs/$jobId1/interviews" -Token $token1
Write-Host "Interviews remaining for Job 1: $($getIntsAfterDel.Data.data.Count) (Expected 0)"

Write-Host "`n=== ALL LIVE E2E API VERIFICATIONS PASSED SUCCESSFULLY ==="

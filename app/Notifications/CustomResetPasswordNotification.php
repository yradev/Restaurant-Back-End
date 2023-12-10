<?php

namespace App\Notifications;

use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Notifications\Messages\MailMessage;
use Illuminate\Notifications\Notification;

class CustomResetPasswordNotification extends Notification
{
    use Queueable;
    protected $token;
    protected $url;
    protected $email;

    /**
     * Create a new notification instance.
     */
    public function __construct($url, $email, $token)
    {
        //
        $this->token = $token;
        $this->url = $url;
        $this->email = $email;
    }

    /**
     * Get the notification's delivery channels.
     *
     * @return array<int, string>
     */
    public function via(object $notifiable): array
    {
        return ['mail'];
    }

    /**
     * Get the mail representation of the notification.
     */
    public function toMail(object $notifiable): MailMessage
    {
        $url = "{$this->url}/{$this->email}/{$this->token}";
        return (new MailMessage)
            ->line(__('email.reset-password-top'))
            ->action(__('email.reset-password-action'), url($url))
            ->line(__('email.reset-password-bottom',['expire' => config('auth.passwords.users.expire')]));
    }

    /**
     * Get the array representation of the notification.
     *
     * @return array<string, mixed>
     */
    public function toArray(object $notifiable): array
    {
        return [
            //
        ];
    }
}

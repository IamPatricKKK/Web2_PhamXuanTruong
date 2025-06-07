// Event handling functions
function editEvent(id) {
    fetch(`/events/${id}`)
        .then(response => response.json())
        .then(event => {
            document.getElementById('eventId').value = event.id;
            document.getElementById('title').value = event.title;
            document.getElementById('description').value = event.description;
            document.getElementById('type').value = event.type.id;
            document.getElementById('isAllDay').checked = event.isAllDay;
            document.getElementById('startTime').value = formatDateTime(event.startTime);
            document.getElementById('endTime').value = formatDateTime(event.endTime);
            document.getElementById('isImportant').checked = event.isImportant;
            document.getElementById('note').value = event.note || '';
            
            if (event.remindTime) {
                document.getElementById('setReminder').checked = true;
                document.getElementById('reminderTimeGroup').style.display = 'block';
                document.getElementById('remindTime').value = formatDateTime(event.remindTime);
            } else {
                document.getElementById('setReminder').checked = false;
                document.getElementById('reminderTimeGroup').style.display = 'none';
            }
            
            document.getElementById('eventModalTitle').textContent = 'Chỉnh Sửa Sự Kiện';
            new bootstrap.Modal(document.getElementById('eventModal')).show();
        });
}

function saveEvent() {
    const eventData = {
        id: document.getElementById('eventId').value || null,
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        type: { id: document.getElementById('type').value },
        isAllDay: document.getElementById('isAllDay').checked,
        startTime: document.getElementById('startTime').value,
        endTime: document.getElementById('endTime').value,
        isImportant: document.getElementById('isImportant').checked,
        note: document.getElementById('note').value,
        remindTime: document.getElementById('setReminder').checked ? 
                   document.getElementById('remindTime').value : null
    };

    const method = eventData.id ? 'PUT' : 'POST';
    const url = eventData.id ? `/events/${eventData.id}` : '/events';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(eventData)
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
        } else {
            alert('Có lỗi xảy ra khi lưu sự kiện');
        }
    });
}

function toggleImportant(id) {
    fetch(`/events/${id}/toggle-important`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
        }
    });
}

function deleteEvent(id) {
    document.getElementById('deleteModal').setAttribute('data-event-id', id);
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

function confirmDelete() {
    const id = document.getElementById('deleteModal').getAttribute('data-event-id');
    fetch(`/events/${id}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            window.location.reload();
        }
    });
}

// Utility functions
function formatDateTime(dateTimeString) {
    if (!dateTimeString) return '';
    const date = new Date(dateTimeString);
    return date.toISOString().slice(0, 16);
}

function editEvent(id) {
    window.location.href = '/events/edit/' + id;
}

function deleteEvent(id) {
    if (confirm('Bạn có chắc muốn xóa sự kiện này?')) {
        window.location.href = '/events/delete/' + id;
    }
}

function toggleImportant(id) {
    window.location.href = '/events/toggle-important/' + id;
}

function toggleAllDay(id) {
    window.location.href = '/events/toggle-allday/' + id;
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    // Handle reminder checkbox
    const setReminderCheckbox = document.getElementById('setReminder');
    const reminderTimeGroup = document.getElementById('reminderTimeGroup');
    
    setReminderCheckbox.addEventListener('change', function() {
        reminderTimeGroup.style.display = this.checked ? 'block' : 'none';
    });

    // Reset form when modal is closed
    document.getElementById('eventModal').addEventListener('hidden.bs.modal', function() {
        document.getElementById('eventForm').reset();
        document.getElementById('eventId').value = '';
        document.getElementById('eventModalTitle').textContent = 'Thêm Sự Kiện Mới';
        document.getElementById('reminderTimeGroup').style.display = 'none';
    });

    // Handle filters
    const typeFilter = document.getElementById('typeFilter');
    const statusFilter = document.getElementById('statusFilter');
    const sortBy = document.getElementById('sortBy');
    const searchEvent = document.getElementById('searchEvent');

    function applyFilters() {
        const type = typeFilter.value;
        const status = statusFilter.value;
        const sort = sortBy.value;
        const search = searchEvent.value;

        window.location.href = `/events?type=${type}&status=${status}&sort=${sort}&search=${search}`;
    }

    typeFilter.addEventListener('change', applyFilters);
    statusFilter.addEventListener('change', applyFilters);
    sortBy.addEventListener('change', applyFilters);
    
    let searchTimeout;
    searchEvent.addEventListener('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(applyFilters, 500);
    });
}); 

